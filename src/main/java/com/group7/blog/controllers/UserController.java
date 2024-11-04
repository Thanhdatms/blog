package com.group7.blog.controllers;


import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.Tag.response.TagResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.models.Blog;
import com.group7.blog.models.Users;
import com.group7.blog.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @GetMapping
    List<Users> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    ApiResponse<Users> createUser(@RequestBody UserCreationRequest request){
        ApiResponse<Users> usersApiResponse = new ApiResponse<>();
        Users createdUser = userService.createUser(request);
        usersApiResponse.setResult(createdUser);

        return usersApiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<Users> getUser(@PathVariable("userId") UUID userId){
        ApiResponse<Users> usersApiResponse = new ApiResponse<>();
        Users user = userService.getUser(userId);
        usersApiResponse.setResult(user);

        return usersApiResponse;

    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") UUID userId, @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> usersApiResponse = new ApiResponse<>();
        UserResponse user = userService.updateUser(userId, request);

        usersApiResponse.setResult(user);
        return usersApiResponse;
    }

    @GetMapping("/me")
    ApiResponse<UserResponse> getMe () {
        return ApiResponse.<UserResponse>builder()
                .message("Get User Profile Successfully!")
                .result(userService.getCurrentUserInfor())
                .build();
    }

    @GetMapping("/{userId}/blogs")
    ApiResponse<UserResponse> getBlogsByUserId(@PathVariable("userId") UUID userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getBlogsByUserId(userId))
                .build();
    }
}
