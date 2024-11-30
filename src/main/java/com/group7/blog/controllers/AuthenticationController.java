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
//        Cookie cookie = authenticationService.getCookie("refresh_token", tokens.getRefreshToken());
        ResponseCookie cookie = ResponseCookie.from("refresh_token", tokens.getRefreshToken()) // key & value
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .sameSite("None")  // sameSite
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ApiResponse.<String>builder()
                .result(tokens.getAccessToken())
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@CookieValue(value = "refresh_token", defaultValue = "") String cookie, HttpServletResponse response) {
        Cookie newCookie = authenticationService.deleteCookie("refresh_token", null);
        response.addCookie(newCookie);
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
