package com.group7.blog.controllers;


import com.group7.blog.dto.Comment.request.CommentCreationRequest;
import com.group7.blog.dto.Comment.request.CommentDeleteRequest;
import com.group7.blog.dto.Comment.request.CommentUpdateRequest;
import com.group7.blog.dto.Comment.response.CommentDetailResponse;
import com.group7.blog.dto.Comment.response.CommentResponse;
import com.group7.blog.dto.Comment.response.CommentStatusResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.services.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blogs/{blogId}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping
    ApiResponse<CommentResponse> createComment(
            @PathVariable UUID blogId,
            @RequestBody @Valid CommentCreationRequest request){
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(blogId, request))
                .build();
    }

    @DeleteMapping("/{commentId}")
    ApiResponse<CommentStatusResponse> deleteComment(
            @PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId
            ){
        return ApiResponse.<CommentStatusResponse>builder()
                .result(commentService.deleteComment(blogId, commentId))
                .build();
    }

    @PutMapping("/{commentId}")
    ApiResponse<CommentStatusResponse> updateComment(
            @PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId,
            @RequestBody @Valid CommentUpdateRequest request
            ){
        return ApiResponse.<CommentStatusResponse>builder()
                .result(commentService.updateComment(blogId, commentId, request.getContent()))
                .build();
    }

    @GetMapping
    ApiResponse<List<CommentDetailResponse>> getBlogDetailComment(
            @PathVariable("blogId") String blogId,
            @RequestParam int page,
            @RequestParam int size
            ){
        return ApiResponse.<List<CommentDetailResponse>>builder()
                .result(commentService.getPaginatedBlogComment(blogId, page, size))
                .build();
    }

}
