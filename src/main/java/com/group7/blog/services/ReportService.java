package com.group7.blog.services;

import com.group7.blog.dto.Report.reponse.ReportResponse;
import com.group7.blog.dto.Report.request.ReportCreationRequest;
import com.group7.blog.dto.Report.request.ReportDetailResponse;
import com.group7.blog.dto.User.reponse.UserInfoResponse;
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
        newReport.setDescription(request.getDescription());
        newReport.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        reportRepository.save(newReport);

        return reportMapper.toResponse(newReport);
    }



    public List<ReportDetailResponse> getListReport(int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

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
}
