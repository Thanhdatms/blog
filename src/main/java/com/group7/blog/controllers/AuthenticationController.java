package com.group7.blog.controllers;


import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.services.AuthenticationService;
import com.group7.blog.services.LoadSampleDataService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    LoadSampleDataService loadSampleDataService;

    @PostMapping("/login")
    ApiResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        TokenResponse tokens = authenticationService.login(request);
        Cookie cookie = authenticationService.getCookie("jwt", tokens.getRefreshToken());
        response.addCookie(cookie);
        return ApiResponse.<String>builder()
                .result(tokens.getAccessToken())
                .build();
    }

    @GetMapping("/token")
    ApiResponse<String> refreshToken(HttpServletRequest request) {
        AtomicReference<String> refreshToken = new AtomicReference<>();
        Arrays.stream(request.getCookies()).forEach(cookie -> {
            if(cookie.getName().equals("jwt")) {
                if(cookie.getValue() == null || cookie.getValue().isEmpty())
                    throw new AppException(ErrorCode.INVALID_TOKEN);
                refreshToken.set(cookie.getValue());
            }
        });
        return ApiResponse.<String>builder()
                .result(String.valueOf(refreshToken))
                .build();
    }

    @PostMapping("/sample-data")
    public String loadSampleData() {
        loadSampleDataService.loadUserData();
        return "Load data successfully!";
    }
}
