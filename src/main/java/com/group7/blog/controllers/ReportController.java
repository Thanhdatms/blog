package com.group7.blog.controllers;


import com.group7.blog.dto.Auth.TokenBinaryDecoded;
import com.group7.blog.dto.Report.reponse.ReportDetailResponse;
import com.group7.blog.dto.Report.reponse.ReportResponse;
import com.group7.blog.dto.Report.request.ReportCreationRequest;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.services.ReportService;
import com.group7.blog.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/reports")
public class ReportController {

    ReportService reportService;
    TokenService tokenService;

    @Operation(
            summary = "Create a report for a blog",
            description = "This endpoint allows you to create a report for a specific blog post.",
            tags = {"Reports"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Report created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping("/{blogId}")
    public ApiResponse<ReportResponse> createReport(
            @PathVariable("blogId") String blogId,
            @RequestBody @Valid ReportCreationRequest request,
            HttpServletRequest req
            ) {
        String decodedToken = (String) req.getAttribute("decodedToken");
        String userId = tokenService.parseDecodedToken(decodedToken).getUserId();
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.create(blogId, request, userId))
                .build();
    }

    @Operation(
            summary = "Get a list of reports",
            description = "This endpoint retrieves a paginated list of reports.",
            tags = {"Reports"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Reports fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping
    public ApiResponse<List<ReportDetailResponse>> getReport(
            @RequestParam int page,
            @RequestParam int size,
            HttpServletRequest req
            ) {
        String decodedToken = (String) req.getAttribute("decodedToken");
        TokenBinaryDecoded result = tokenService.parseDecodedToken(decodedToken);
        String userId = result.getUserId();
        boolean isAdmin = result.getRoles().stream().anyMatch(roleResponse -> "admin".equals(roleResponse.getName()));
        if(!isAdmin) throw new AppException(ErrorCode.UNAUTHORIZED);
        return ApiResponse.<List<ReportDetailResponse>>builder()
                .result(reportService.getListReport(page, size, userId))
                .build();
    }

    @Operation(
            summary = "Get a list of reports for a specific user",
            description = "This endpoint retrieves a paginated list of reports for a user.",
            tags = {"Reports"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User's reports fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping("/user")
    public ApiResponse<List<ReportDetailResponse>> getUserReport(
            @RequestParam String username,
            @RequestParam int page,
            @RequestParam int size,
            HttpServletRequest req
    ) {
        String decodedToken = (String) req.getAttribute("decodedToken");
        TokenBinaryDecoded result = tokenService.parseDecodedToken(decodedToken);
        String userId = result.getUserId();
        boolean isAdmin = result.getRoles().stream().anyMatch(roleResponse -> "admin".equals(roleResponse.getName()));
        if(!isAdmin) throw new AppException(ErrorCode.UNAUTHORIZED);
        return ApiResponse.<List<ReportDetailResponse>>builder()
                .result(reportService.getListUserReport(username, page, size, userId))
                .build();
    }

    @Operation(
            summary = "Update the status of a report",
            description = "This endpoint allows you to update the status of a report (e.g., mark as resolved).",
            tags = {"Reports"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Report status updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid status or report ID", content = @Content)
            }
    )
    @PutMapping("/{reportId}")
    public ApiResponse<ReportResponse> updateStatus(
            @RequestParam String reportStatus,
            @PathVariable("reportId") String reportId,
            HttpServletRequest req) {
        String decodedToken = (String) req.getAttribute("decodedToken");
        TokenBinaryDecoded result = tokenService.parseDecodedToken(decodedToken);
        String userId = result.getUserId();
        boolean isAdmin = result.getRoles().stream().anyMatch(roleResponse -> "admin".equals(roleResponse.getName()));
        if(!isAdmin) throw new AppException(ErrorCode.UNAUTHORIZED);
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.updateReportStatus(reportId, reportStatus, userId))
                .build();
    }
}
