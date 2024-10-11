package com.group7.blog.controllers;

import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.reponse.ApiResponse;
import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.services.BlogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {
    BlogService blogService;

    @PostMapping
    ApiResponse<BlogResponse> createBlog(
            @RequestPart("blog") BlogCreationRequest request,
            @RequestPart("file") MultipartFile file
            ) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.createBlog(request, file))
                .build();
    }

    @GetMapping
    ApiResponse<List<BlogResponse>> getBlogs() {
        return ApiResponse.<List<BlogResponse>>builder()
                .result(blogService.getBlogs())
                .build();
    }

    @GetMapping("/{blogId}")
    ApiResponse<BlogResponse> getBlog(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.getBlog(blogId))
                .build();
    }

    @PutMapping("/{blogId}")
    ApiResponse<BlogResponse> updateBlog(@PathVariable("blogId") UUID blogId, @RequestBody BlogUpdateRequest request) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.updateBlog(blogId, request))
                .build();
    }
}
