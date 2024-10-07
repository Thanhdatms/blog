package com.group7.blog.controllers;


import com.group7.blog.dto.reponse.ApiResponse;
import com.group7.blog.dto.request.UserCreationRequest;
import com.group7.blog.enums.StatusCode;
import com.group7.blog.models.Users;
import com.group7.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    List<Users> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    ApiResponse<Users> createUser(@RequestBody UserCreationRequest request){
        ApiResponse<Users> usersApiResponse = new ApiResponse<>();
        Users createdUser = userService.createUser(request);

        usersApiResponse.setCode(StatusCode.SUCCESS.getCode());
        usersApiResponse.setMessage(StatusCode.SUCCESS.getMessage());
        usersApiResponse.setMetadata(createdUser);

        return usersApiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<Users> getUser(@PathVariable("userId") UUID userId){
        ApiResponse<Users> usersApiResponse = new ApiResponse<>();
        Users user = userService.getUser(userId);

        usersApiResponse.setCode(StatusCode.SUCCESS.getCode());
        usersApiResponse.setMessage(StatusCode.SUCCESS.getMessage());
        usersApiResponse.setMetadata(user);

        return usersApiResponse;

    }


}
