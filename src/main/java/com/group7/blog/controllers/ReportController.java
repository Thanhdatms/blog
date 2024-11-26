package com.group7.blog.controllers;


import com.group7.blog.dto.Report.reponse.ReportResponse;
import com.group7.blog.dto.Report.request.ReportCreationRequest;
import com.group7.blog.dto.Report.request.ReportDetailResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.services.ReportService;
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

    @PostMapping("/{blogId}")
    ApiResponse<ReportResponse> createReport(
            @PathVariable("blogId") String blogId,
            @RequestBody @Valid ReportCreationRequest request
            ){
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.create(blogId, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ReportDetailResponse>> getReport(
            @RequestParam int page,
            @RequestParam int size
            ){
        return ApiResponse.<List<ReportDetailResponse>>builder()
                .result(reportService.getListReport(page,size))
                .build();
    }

}
