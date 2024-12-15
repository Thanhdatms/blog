package com.group7.blog.services;

import com.group7.blog.dto.Report.reponse.ReportDetailResponse;
import com.group7.blog.dto.Report.reponse.ReportResponse;
import com.group7.blog.dto.Report.request.ReportCreationRequest;
import com.group7.blog.enums.EnumData;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.ReportMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.models.Report;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.BlogRepository;
import com.group7.blog.repositories.ReportRepository;
import com.group7.blog.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ReportService {
    ReportRepository reportRepository;
    UserRepository userRepository;
    BlogRepository blogRepository;

    ReportMapper reportMapper;

    public ReportResponse create(String blogId, ReportCreationRequest request){
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        Users user = userRepository
                .findById(UUID
                        .fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Blog blog = blogRepository
                .findById(UUID
                        .fromString(blogId))
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));

        // Create report
        Report newReport = new Report();

        newReport.setBlog(blog);
        newReport.setUsers(user);
        newReport.setReportType(request.getReportType());
        newReport.setReportReason(request.getReportReason());
        newReport.setDescription(request.getDescription());
        newReport.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        reportRepository.save(newReport);

        return reportMapper.toResponse(newReport);
    }

    public List<ReportDetailResponse> getListReport(int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> "admin".equals(authority.getAuthority()));
        if(!isAdmin) throw new AppException(ErrorCode.UNAUTHORIZED);
        // Validate User
        userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Pagination with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Fetch paginated Reports
        Page<Report> paginatedReports = reportRepository.findAll(pageable);

        // Map reports to responses
        return paginatedReports.getContent().stream()
                .map(reportMapper::toReportDetailResponse)
                .collect(Collectors.toList());
    }

    public List<ReportDetailResponse> getListUserReport(String username, int page, int size){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> "admin".equals(authority.getAuthority()));
        if(!isAdmin) throw new AppException(ErrorCode.UNAUTHORIZED);
        userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Report> reportPage = reportRepository.findByUsers(user, pageable);

        return reportPage.getContent().stream()
                .map(reportMapper::toReportDetailResponse)
                .collect(Collectors.toList());
    }

    public ReportResponse updateReportStatus(String reportID, String reportStatus){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> "admin".equals(authority.getAuthority()));
        if(!isAdmin) throw new AppException(ErrorCode.UNAUTHORIZED);
        userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Report report = reportRepository.findById(Integer.valueOf(reportID))
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_EXIST));
        Blog blog = blogRepository.findById(report.getBlog().getId())
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));
        Users user = userRepository.findById(UUID.fromString(userId))
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        report.setReportStatus(EnumData.ReportStatus.valueOf(reportStatus));
        if(report.getReportStatus() == EnumData.ReportStatus.DELETE) {
            if(report.getReportType() == EnumData.ReportType.BLOG) {
                blog.setBlogStatus(EnumData.BlogStatus.BANNED);
            } else if(report.getReportType() == EnumData.ReportType.USER) {
                user.setUserStatus(EnumData.UserStatus.BANNED);
            }
        } else if(report.getReportStatus() == EnumData.ReportStatus.CANCEL) {
            if(report.getReportType() == EnumData.ReportType.BLOG) {
                blog.setBlogStatus(EnumData.BlogStatus.PUBLISHED);
            } else if(report.getReportType() == EnumData.ReportType.USER) {
                user.setUserStatus(EnumData.UserStatus.ACTIVATED);
            }
        }
        blogRepository.save(blog);

        reportRepository.save(report);

        return reportMapper.toResponse(report);
    }

    public List<ReportDetailResponse> getListReportByStatus(String reportStatus, String reportType, int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> "admin".equals(authority.getAuthority()));
        if(!isAdmin) throw new AppException(ErrorCode.UNAUTHORIZED);

        userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Pagination with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Convert string to enum
        EnumData.ReportStatus status = EnumData.ReportStatus.valueOf(reportStatus.toUpperCase());

        EnumData.ReportType type = EnumData.ReportType.valueOf(reportType.toUpperCase());

        // Fetch paginated Reports by status
        Page<Report> paginatedReports = reportRepository.findByReportStatusAndReportType(status, type, pageable);

        // Map reports to responses
        return paginatedReports.getContent().stream()
                .map(reportMapper::toReportDetailResponse)
                .collect(Collectors.toList());
    }
}
