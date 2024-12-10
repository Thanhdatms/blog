package com.group7.blog.controllers;


import com.group7.blog.dto.Report.reponse.ReportResponse;
import com.group7.blog.dto.Report.request.ReportCreationRequest;
import com.group7.blog.dto.Report.reponse.ReportDetailResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @RequestBody @Valid ReportCreationRequest request) {
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.create(blogId, request))
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
            @RequestParam int size) {
        return ApiResponse.<List<ReportDetailResponse>>builder()
                .result(reportService.getListReport(page, size))
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
            @RequestParam int size) {
        return ApiResponse.<List<ReportDetailResponse>>builder()
                .result(reportService.getListUserReport(username, page, size))
                .build();
    }

    @Operation(
            summary = "Update the status of a report",
            description = "Updates the status of a specific report (e.g., mark it as resolved).",
            tags = {"Reports"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Report status updated successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))
                    )
            }
    )
    @PutMapping("/{reportId}")
    public ApiResponse<ReportResponse> updateStatus(
            @RequestParam("reportStatus") String reportStatus,
            @PathVariable("reportId") String reportId) {
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.updateReportStatus(reportId, reportStatus))
                .build();
    }
}
