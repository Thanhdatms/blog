package com.group7.blog.controllers;


import com.group7.blog.dto.BookMark.response.BookMarkListResponse;
import com.group7.blog.dto.BookMark.response.BookMarkResponse;
import com.group7.blog.dto.User.reponse.*;
import com.group7.blog.dto.User.request.*;
import com.group7.blog.models.Users;
import com.group7.blog.services.BookMarkService;
import com.group7.blog.services.UserBlogVoteService;
import com.group7.blog.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(
            summary = "Create a User",
            description = "Creates a new user. Optionally, you can include a profile image.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @PostMapping
    ApiResponse<UserResponse> createUser(
            @Valid @RequestPart("user") UserCreationRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request, file))
                .build();
    }

    @Operation(
            summary = "Follow a User",
            description = "Allows the current user to follow a target user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User followed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping("/followers/user")
    ApiResponse<String> followUser(@RequestBody UserFollowRequest request) {
        return ApiResponse.<String>builder()
                .result(userService.followUser(request.getTargetUserId()))
                .build();
    }

    @Operation(
            summary = "Reset User Password",
            description = "Resets the password for the user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping("/reset-password")
    ApiResponse<String> resetPassword(@RequestBody ResetPasswordDTO request) {
        return ApiResponse.<String>builder()
                .result(userService.resetPassword(request))
                .build();
    }

    @Operation(
            summary = "Change User Password",
            description = "Changes the password for the user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password changed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping("/change-password")
    ApiResponse<String> changePassword(@RequestBody ChangePasswordDTO request) {
        return ApiResponse.<String>builder()
                .result(userService.changePassword(request))
                .build();
    }

    @Operation(
            summary = "Get All Users",
            description = "Fetches a list of all users.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    @GetMapping
    List<Users> getUsers() {
        return userService.getUsers();
    }

    @Operation(
            summary = "Get Current User Profile",
            description = "Fetches the profile details of the current user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponseDTO.class)))
    @GetMapping("/me")
    ApiResponse<UserProfileResponseDTO> getMe () {
        return ApiResponse.<UserProfileResponseDTO>builder()
                .message("Get User Profile Successfully!")
                .result(userService.getCurrentUserInfo())
                .build();
    }

    @Operation(
            summary = "Get User Profile by Name Tag",
            description = "Fetches the user profile by the provided name tag.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponseDTO.class)))
    @GetMapping("/{nameTag}")
    ApiResponse<UserProfileResponseDTO> getUserProfile(@PathVariable("nameTag") String nameTag){
        return ApiResponse.<UserProfileResponseDTO>builder()
                .result(userService.getUserByNameTag(nameTag))
                .build();
    }

    @Operation(
            summary = "Get Blogs by User ID",
            description = "Fetches the blogs created by a specific user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User blogs fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @GetMapping("/{userId}/blogs")
    ApiResponse<UserResponse> getBlogsByUserId(@PathVariable("userId") UUID userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getBlogsByUserId(userId))
                .build();
    }

    @Operation(
            summary = "Check if User is Following",
            description = "Checks if the current user is following the specified user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Following status fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    @GetMapping("/followers/user/{userId}/is-following")
    ApiResponse<Boolean> isFollowing(@PathVariable("userId") UUID userId) {
        return ApiResponse.<Boolean>builder()
                .result(userService.isFollowing(userId))
                .build();
    }

    @Operation(
            summary = "Verify Reset Password Token",
            description = "Verifies the reset password token sent to the user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reset password token verified", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @GetMapping("/reset-password")
    ApiResponse<String> verifyResetPasswordToken(@RequestParam(name = "token", required = true) String token) {
        return ApiResponse.<String>builder()
                .result(userService.verifyResetPasswordToken(token))
                .build();
    }

    @Operation(
            summary = "Get User Stats",
            description = "Fetches statistical data related to a specific user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User stats fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserStatsResponseDTO.class)))
    @GetMapping("/stats/{userId}")
    ApiResponse<UserStatsResponseDTO> getUserStats(@PathVariable("userId") UUID userId) {
        return ApiResponse.<UserStatsResponseDTO>builder()
                .result(userService.getUserStats(userId))
                .build();
    }

    @Operation(
            summary = "Get Top Users",
            description = "Fetches a list of top users based on some criteria.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top users fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    @GetMapping("/insight")
    public ApiResponse<List<TopUserResponse>> getTopUsers() {
        return ApiResponse.<List<TopUserResponse>>builder()
                .result(userService.getTopUsers())
                .build();
    }

    @Operation(
            summary = "Update User Profile",
            description = "Updates the profile details of a specific user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") UUID userId, @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> usersApiResponse = new ApiResponse<>();
        UserResponse user = userService.updateUser(userId, request);
        usersApiResponse.setResult(user);
        return usersApiResponse;
    }

    @Operation(
            summary = "Update User Profile with Image",
            description = "Updates the user's profile with optional image upload.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponseDTO.class)))
    @PutMapping("/profiles")
    ApiResponse<UserProfileResponseDTO> updateProfile(
            @Valid @RequestPart("userProfile") UpdateProfileRequestDTO request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        return ApiResponse.<UserProfileResponseDTO>builder()
                .result(userService.updateProfile(request, file))
                .build();
    }

    @Operation(
            summary = "Add a Blog to Bookmarks",
            description = "Adds a blog to the user's bookmarks.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog added to bookmarks", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookMarkResponse.class)))
    @PutMapping("/bookmarks/blog/{blogId}")
    ApiResponse<BookMarkResponse> addBookMark(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<BookMarkResponse>builder()
                .result(bookMarkService.addBookMark(blogId))
                .build();
    }

    @Operation(
            summary = "Unfollow a User",
            description = "Allows the current user to unfollow a target user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User unfollowed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @DeleteMapping("/followers/user")
    ApiResponse<String> unFollowUser(@RequestBody UserFollowRequest request) {
        return ApiResponse.<String>builder()
                .result(userService.unFollowUser(request.getTargetUserId()))
                .build();
    }

    @Operation(
            summary = "Remove Blog from Bookmarks",
            description = "Removes a blog from the user's bookmarks.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog removed from bookmarks", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @DeleteMapping("/bookmarks/blog/{blogId}")
    ApiResponse<String> removeBookMark(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<String>builder()
                .result(bookMarkService.removeBookMark(blogId))
                .build();
    }

    @Operation(
            summary = "Check if Blog is Bookmarked",
            description = "Checks if a specific blog is bookmarked by the current user.",
            tags = {"Users"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog bookmark status checked", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    @GetMapping("/bookmarks/blog/{blogId}/is-bookmarked")
    ApiResponse<Boolean> isBookMarked(@PathVariable("blogId") UUID blogId) {
        return ApiResponse.<Boolean>builder()
                .result(bookMarkService.isBookMarked(blogId))
                .build();
    }

}
