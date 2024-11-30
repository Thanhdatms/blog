package com.group7.blog.controllers;


import com.group7.blog.dto.Comment.request.CommentCreationRequest;
import com.group7.blog.dto.Comment.request.CommentDeleteRequest;
import com.group7.blog.dto.Comment.request.CommentUpdateRequest;
import com.group7.blog.dto.Comment.response.CommentDetailResponse;
import com.group7.blog.dto.Comment.response.CommentResponse;
import com.group7.blog.dto.Comment.response.CommentStatusResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(
            summary = "Create a new comment",
            description = "This endpoint allows you to create a new comment on a specific blog.",
            tags = {"Comments"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Comment created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping
    public ApiResponse<CommentResponse> createComment(
            @PathVariable UUID blogId,
            @RequestBody @Valid CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(blogId, request))
                .build();
    }

    @Operation(
            summary = "Delete a comment",
            description = "This endpoint allows you to delete a comment from a specific blog.",
            tags = {"Comments"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Comment deleted successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentStatusResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
            }
    )
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentStatusResponse> deleteComment(
            @PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId) {
        return ApiResponse.<CommentStatusResponse>builder()
                .result(commentService.deleteComment(blogId, commentId))
                .build();
    }

    @Operation(
            summary = "Update a comment",
            description = "This endpoint allows you to update a comment on a specific blog.",
            tags = {"Comments"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Comment updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentStatusResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PutMapping("/{commentId}")
    public ApiResponse<CommentStatusResponse> updateComment(
            @PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId,
            @RequestBody @Valid CommentUpdateRequest request) {
        return ApiResponse.<CommentStatusResponse>builder()
                .result(commentService.updateComment(blogId, commentId, request.getContent()))
                .build();
    }

    @Operation(
            summary = "Get comments of a blog",
            description = "This endpoint retrieves a paginated list of comments for a specific blog.",
            tags = {"Comments"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Comments fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping
    public ApiResponse<List<CommentDetailResponse>> getBlogDetailComment(
            @PathVariable("blogId") String blogId,
            @RequestParam int page,
            @RequestParam int size) {
        return ApiResponse.<List<CommentDetailResponse>>builder()
                .result(commentService.getPaginatedBlogComment(blogId, page, size))
                .build();
    }
}
