package com.group7.blog.controllers;


import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        TokenResponse tokens = authenticationService.login(request);
        ResponseCookie cookie = authenticationService.getCookie("refresh_token", tokens.getRefreshToken());
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ApiResponse.<String>builder()
                .result(tokens.getAccessToken())
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@CookieValue(value = "refresh_token", defaultValue = "") String cookie, HttpServletResponse response) {
        ResponseCookie deletedCookie = authenticationService.deleteCookie("refresh_token");
        response.setHeader(HttpHeaders.SET_COOKIE, String.valueOf(deletedCookie));
        return ApiResponse.<String>builder()
                .result(authenticationService.logOut(cookie))
                .build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<String> refreshToken(@CookieValue(value = "refresh_token", defaultValue = "") String cookie) {
        return ApiResponse.<String>builder()
                .result(authenticationService.getNewAccessToken(cookie))
                .build();
    }
}
