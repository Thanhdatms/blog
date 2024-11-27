package com.group7.blog.controllers;

import com.group7.blog.dto.Blog.response.BlogDetailResponse;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.BlogTag.BlogTagResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.models.Blog;
import com.group7.blog.services.BlogService;
import com.group7.blog.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    TokenService tokenService;

    @PostMapping
    ApiResponse<BlogDetailResponse> createBlog(
            @Valid @RequestPart("blog") BlogCreationRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file,
            HttpServletRequest req
            ) {
        String decodedToken = (String) req.getAttribute("decodedToken");
        String userId = tokenService.parseDecodedToken(decodedToken).getUserId();
        return ApiResponse.<BlogDetailResponse>builder()
                .result(blogService.createBlog(request, file, userId))
                .build();
    }

    @GetMapping
    ApiResponse<List<BlogDetailResponse>> getBlogs() {
        return ApiResponse.<List<BlogDetailResponse>>builder()
                .result(blogService.getBlogs())
                .build();
    }

    @GetMapping("/{blogId}")
    ApiResponse<BlogDetailResponse> getBlog(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<BlogDetailResponse>builder()
                .result(blogService.getBlog(blogId))
                .build();
    }

    @PutMapping("/{blogId}")
    ApiResponse<BlogResponse> updateBlog(
            @PathVariable("blogId") UUID blogId,
            @Valid @RequestPart("blog") BlogUpdateRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.updateBlog(blogId, request, file))
                .build();
    }
}
