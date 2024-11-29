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

    @PostMapping
    ApiResponse<BlogDetailResponse> createBlog(
            @Valid @RequestPart("blog") BlogCreationRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
            ) {
        return ApiResponse.<BlogDetailResponse>builder()
                .result(blogService.createBlog(request, file))
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

    @PostMapping("/{blogId}/user/votes")
    ApiResponse<BlogVoteResponse> upOrDownVote(@RequestParam(name="voteType", required = true) EnumData.VoteType voteType, @PathVariable("blogId") UUID blogId) {
        return ApiResponse.<BlogVoteResponse>builder()
                .result(userBlogVoteService.create(voteType, blogId))
                .build();
    }

    @DeleteMapping("/{blogId}/user/votes")
    ApiResponse<String> removeVote(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<String>builder()
                .result(userBlogVoteService.delete(blogId))
                .build();
    }

    @GetMapping("/{blogId}/users/votes")
    ApiResponse<List<UserProfileResponseDTO>> getListUsersVotes(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<List<UserProfileResponseDTO>>builder()
                .result(userBlogVoteService.getUserVotesByBlogId(blogId))
                .build();
    }

    @GetMapping("/{blogId}/user/is-voted")
    ApiResponse<BlogVoteResponse> isVote(@PathVariable("blogId") UUID blogId) {
        return  ApiResponse.<BlogVoteResponse>builder()
                .result(userBlogVoteService.checkIsVoted(blogId))
                .build();
    }

    @GetMapping("/searchblogs")
    ApiResponse<List<BlogResponse>> searchBlog(
            @RequestParam("keyword") String keyword,
            @RequestParam("size") int size,
            @RequestParam("page") int page
    ){
        return ApiResponse.<List<BlogResponse>>builder()
                .result(blogService.searchBlog(keyword, page, size))
                .build();
    }

    @PostMapping("/images/upload")
    ImageUploadResponseDTO uploadBlogImage(@RequestPart(name = "upload", required = false) MultipartFile file) throws ImageUploadException {
        if (file == null || file.isEmpty()) {
            throw new ImageUploadException(ErrorCode.FILE_MISSING.getMessage());
        }
        ImageUploadResponseDTO result = new ImageUploadResponseDTO();
        result.setUrl(cloudinaryService.uploadFile(file, FOLDER_NAME));
        return result;
    }
}
