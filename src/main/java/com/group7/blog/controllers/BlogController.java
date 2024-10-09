package com.group7.blog.controllers;

import com.group7.blog.dto.reponse.ApiResponse;
import com.group7.blog.dto.request.BlogCreationRequest;
import com.group7.blog.dto.request.BlogUpdateRequest;
import com.group7.blog.models.Blog;
import com.group7.blog.services.BlogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {
    BlogService blogService;

    @PostMapping
    ApiResponse<Blog> createBlog(@RequestBody BlogCreationRequest request) {
        return ApiResponse.<Blog>builder()
                .result(blogService.createBlog(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<Blog>> getBlogs() {
        return ApiResponse.<List<Blog>>builder()
                .result(blogService.getBlogs())
                .build();
    }

    @GetMapping("/{blogId}")
    ApiResponse<Blog> getBlog(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<Blog>builder()
                .result(blogService.getBlog(blogId))
                .build();
    }

    @PutMapping("/{blogId}")
    ApiResponse<Blog> updateBlog(@PathVariable("blogId") UUID blogId, @RequestBody BlogUpdateRequest request) {
        return ApiResponse.<Blog>builder()
                .result(blogService.updateBlog(blogId, request))
                .build();
    }
}
