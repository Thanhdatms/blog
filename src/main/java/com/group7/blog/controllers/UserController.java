package com.group7.blog.controllers;


import com.group7.blog.dto.BookMark.response.BookMarkListResponse;
import com.group7.blog.dto.BookMark.response.BookMarkResponse;
import com.group7.blog.dto.User.reponse.*;
import com.group7.blog.dto.User.request.*;
import com.group7.blog.models.Users;
import com.group7.blog.services.BookMarkService;
import com.group7.blog.services.UserBlogVoteService;
import com.group7.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;
    BookMarkService bookMarkService;
    UserBlogVoteService userBlogVoteService;

    @GetMapping
    List<Users> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    ApiResponse<UserResponse> createUser(
            @Valid @RequestPart("user") UserCreationRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request, file))
                .build();
    }

//    @GetMapping("/{userId}")
//    ApiResponse<UserProfileResponseDTO> getUserProfile(@PathVariable("userId") UUID userId){
//        System.out.println(userId);
//        return ApiResponse.<UserProfileResponseDTO>builder()
//                .result(userService.getUserById(userId))
//                .build();
//    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") UUID userId, @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> usersApiResponse = new ApiResponse<>();
        UserResponse user = userService.updateUser(userId, request);

        usersApiResponse.setResult(user);
        return usersApiResponse;
    }

    @GetMapping("/me")
    ApiResponse<UserProfileResponseDTO> getMe () {
        return ApiResponse.<UserProfileResponseDTO>builder()
                .message("Get User Profile Successfully!")
                .result(userService.getCurrentUserInfo())
                .build();
    }

    @GetMapping("/{userId}/blogs")
    ApiResponse<UserResponse> getBlogsByUserId(@PathVariable("userId") UUID userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getBlogsByUserId(userId))
                .build();
    }

    @PostMapping("/followers/user")
    ApiResponse<String> followUser(@RequestBody UserFollowRequest request) {
        return ApiResponse.<String>builder()
                .result(userService.followUser(request.getTargetUserId()))
                .build();
    }

    @DeleteMapping("/followers/user")
    ApiResponse<String> unFollowUser(@RequestBody UserFollowRequest request) {
        return ApiResponse.<String>builder()
                .result(userService.unFollowUser(request.getTargetUserId()))
                .build();
    }

    @GetMapping("/followers/user/{userId}/is-following")
    ApiResponse<Boolean> isFollowing(@PathVariable("userId") UUID userId) {
        return ApiResponse.<Boolean>builder()
                .result(userService.isFollowing(userId))
                .build();
    }

    @PutMapping("/bookmarks/blog/{blogId}")
    ApiResponse<BookMarkResponse> addBookMark(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<BookMarkResponse>builder()
                .result(bookMarkService.addBookMark(blogId))
                .build();
    }

    @DeleteMapping("/bookmarks/blog/{blogId}")
    ApiResponse<String> removeBookMark(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<String>builder()
                .result(bookMarkService.removeBookMark(blogId))
                .build();
    }

    @GetMapping("/bookmarks")
    ApiResponse<BookMarkListResponse> getBookMarkBlogs () {
        return  ApiResponse.<BookMarkListResponse>builder()
                .result(bookMarkService.getBookMarkBlogs())
                .build();
    }

    @GetMapping("/bookmarks/blog/{blogId}/is-bookmarked")
    ApiResponse<Boolean> isBookMarked(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<Boolean>builder()
                .result(bookMarkService.isBookMarked(blogId))
                .build();
    }

    @PostMapping("/reset-password")
    ApiResponse<String> resetPassword(@RequestBody ResetPasswordDTO request) {
        return ApiResponse.<String>builder()
                .result(userService.resetPassword(request))
                .build();
    }

    @GetMapping("/reset-password")
    ApiResponse<String> verifyResetPasswordToken(@RequestParam(name = "token", required = true) String token) {
        return ApiResponse.<String>builder()
                .result(userService.verifyResetPasswordToken(token))
                .build();
    }

    @PostMapping("/change-password")
    ApiResponse<String> changePassword(@RequestBody ChangePasswordDTO request) {
        return ApiResponse.<String>builder()
                .result(userService.changePassword(request))
                .build();
    }

    @PutMapping("/profiles")
    ApiResponse<UserProfileResponseDTO> updateProfile(
            @Valid @RequestPart("userProfile") UpdateProfileRequestDTO request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        return ApiResponse.<UserProfileResponseDTO>builder()
                .result(userService.updateProfile(request, file))
                .build();
    }

    @GetMapping("/stats/{userId}")
    ApiResponse<UserStatsResponseDTO> getUserStats(@PathVariable("userId") UUID userId) {
        return  ApiResponse.<UserStatsResponseDTO>builder()
                .result(userService.getUserStats(userId))
                .build();
    }

    @GetMapping("/{nameTag}")
    ApiResponse<UserProfileResponseDTO> getUserProfile(@PathVariable("nameTag") String nameTag){
        return ApiResponse.<UserProfileResponseDTO>builder()
                .result(userService.getUserByNameTag(nameTag))
                .build();
    }

    @GetMapping("/insight")
    public ApiResponse<List<TopUserResponse>> getTopUsers() {
        return ApiResponse.<List<TopUserResponse>>builder()
                .result(userService.getTopUsers())
                .build();
    }
}
