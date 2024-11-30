package com.group7.blog.controllers;

import com.group7.blog.dto.Blog.response.BlogDetailResponse;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.Blog.response.ImageUploadResponseDTO;
import com.group7.blog.dto.ImageUpload.response.ErrorMessageDTO;
import com.group7.blog.dto.ImageUpload.response.ImageUploadErrorApiResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.dto.User.reponse.UserProfileResponseDTO;
import com.group7.blog.dto.UserBlogVote.response.BlogVoteResponse;
import com.group7.blog.enums.EnumData;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.exceptions.ImageUploadException;
import com.group7.blog.services.BlogService;
import com.group7.blog.services.CloudinaryService;
import com.group7.blog.services.UserBlogVoteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.group7.blog.enums.Constant.FOLDER_NAME;


@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {
    BlogService blogService;
    UserBlogVoteService userBlogVoteService;
    CloudinaryService cloudinaryService;

    @Operation(
            summary = "Create a Blog",
            description = "Creates a new blog post. Optionally, you can include an image file.",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Blog created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ApiResponse<BlogDetailResponse> createBlog(
            @Valid @RequestPart("blog") BlogCreationRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        return ApiResponse.<BlogDetailResponse>builder()
                .result(blogService.createBlog(request, file))
                .build();
    }

    @Operation(
            summary = "Upvote or Downvote a Blog",
            description = "Allows a user to upvote or downvote a specific blog.",
            tags = {"Blog Votes"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vote recorded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blog not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{blogId}/user/votes")
    public ApiResponse<BlogVoteResponse> upOrDownVote(
            @RequestParam(name = "voteType", required = true) EnumData.VoteType voteType,
            @PathVariable("blogId") UUID blogId
    ) {
        return ApiResponse.<BlogVoteResponse>builder()
                .result(userBlogVoteService.create(voteType, blogId))
                .build();
    }

    @Operation(
            summary = "Upload Blog Image",
            description = "Uploads an image file for use in a blog.",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "File missing or invalid"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/images/upload")
    ImageUploadResponseDTO uploadBlogImage(@RequestPart(name = "upload", required = false) MultipartFile file) throws ImageUploadException {
        if (file == null || file.isEmpty()) {
            throw new ImageUploadException(ErrorCode.FILE_MISSING.getMessage());
        }
        ImageUploadResponseDTO result = new ImageUploadResponseDTO();
        result.setUrl(cloudinaryService.uploadFile(file, FOLDER_NAME));
        return result;
    }

    @Operation(
            summary = "Get All Published Blogs",
            description = "Fetches all published blogs.",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ApiResponse<List<BlogDetailResponse>> getBlogs() {
        return ApiResponse.<List<BlogDetailResponse>>builder()
                .result(blogService.getPublishedBlogs())
                .build();
    }

    @Operation(
            summary = "Search Blogs",
            description = "Searches for blogs based on a keyword with pagination.",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid query parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/searchblogs")
    public ApiResponse<List<BlogResponse>> searchBlog(
            @RequestParam("keyword") String keyword,
            @RequestParam("size") int size,
            @RequestParam("page") int page
    ) {
        return ApiResponse.<List<BlogResponse>>builder()
                .result(blogService.searchBlog(keyword, page, size))
                .build();
    }

    @Operation(
            summary = "Get Blog by ID",
            description = "Fetches details of a specific blog by its ID.",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blog not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{blogId}")
    public ApiResponse<BlogDetailResponse> getBlog(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<BlogDetailResponse>builder()
                .result(blogService.getBlog(blogId))
                .build();
    }

    @Operation(
            summary = "Get All Banned Blogs",
            description = "Fetches all banned blogs.",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/banned")
    public ApiResponse<List<BlogDetailResponse>> getBannedBlogs() {
        return ApiResponse.<List<BlogDetailResponse>>builder()
                .result(blogService.getBannedBlogs())
                .build();
    }

    @Operation(
            summary = "Get List of Users' Votes",
            description = "Fetches a list of users who have voted on a specific blog.",
            tags = {"Blog Votes"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users' votes retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blog not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{blogId}/users/votes")
    public ApiResponse<List<UserProfileResponseDTO>> getListUsersVotes(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<List<UserProfileResponseDTO>>builder()
                .result(userBlogVoteService.getUserVotesByBlogId(blogId))
                .build();
    }

    @Operation(
            summary = "Check if User has Voted",
            description = "Checks if the current user has voted on a specific blog.",
            tags = {"Blog Votes"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vote status retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blog not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{blogId}/user/is-voted")
    public ApiResponse<BlogVoteResponse> isVote(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<BlogVoteResponse>builder()
                .result(userBlogVoteService.checkIsVoted(blogId))
                .build();
    }

    @Operation(
            summary = "Update Blog",
            description = "Updates a blog's details. Optionally, you can include a new image file.",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blog not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{blogId}")
    public ApiResponse<BlogResponse> updateBlog(
            @PathVariable("blogId") UUID blogId,
            @Valid @RequestPart("blog") BlogUpdateRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.updateBlog(blogId, request, file))
                .build();
    }

    @Operation(
            summary = "Update Blog Status",
            description = "Updates the status of a blog (e.g., published, banned).",
            tags = {"Blogs"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blog not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("status/{blogId}")
    public ApiResponse<BlogResponse> updateBlogStatus(
            @RequestParam("blogStatus") String blogStatus,
            @PathVariable("blogId") String blogId
    ) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.updateBlogStatus(blogStatus, blogId))
                .build();
    }

    @Operation(
            summary = "Remove Vote from a Blog",
            description = "Removes a user's vote (upvote or downvote) from a specific blog.",
            tags = {"Blog Votes"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vote removed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blog not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{blogId}/user/votes")
    public ApiResponse<String> removeVote(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<String>builder()
                .result(userBlogVoteService.delete(blogId))
                .build();
    }

}
