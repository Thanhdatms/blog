package com.group7.blog.controllers;


import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.services.AuthenticationService;
import com.group7.blog.services.UserService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    UserService userService;

    @Operation(
            summary = "Register New Account",
            description = "Registers a new user account and optionally uploads a profile picture.",
            tags = {"Authentication"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User successfully registered",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class))
    )
    @PostMapping("/register")
    ApiResponse<UserResponse> registerNewAccount(
            @Valid @RequestPart("user") UserCreationRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request, file))
                .build();
    }

    @Operation(
            summary = "Login",
            description = "Authenticates the user and provides a JWT token if credentials are valid.",
            tags = {"Authentication"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    ApiResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        TokenResponse tokens = authenticationService.login(request);
        ResponseCookie cookie = authenticationService.getCookie("refresh_token", tokens.getRefreshToken());
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ApiResponse.<String>builder()
                .result(tokens.getAccessToken())
                .build();
    }

    @Operation(
            summary = "Logout",
            description = "Logs out the user by invalidating the refresh token and clearing the authentication cookie.",
            tags = {"Authentication"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "No refresh token found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/logout")
    ApiResponse<String> logout(@CookieValue(value = "refresh_token", defaultValue = "") String cookie, HttpServletResponse response) {
        ResponseCookie deletedCookie = authenticationService.deleteCookie("refresh_token");
        response.setHeader(HttpHeaders.SET_COOKIE, String.valueOf(deletedCookie));
        return ApiResponse.<String>builder()
                .result(authenticationService.logOut(cookie))
                .build();
    }

    @Operation(
            summary = "Refresh Access Token",
            description = "Generates a new access token based on a valid refresh token stored in the user's cookie.",
            tags = {"Authentication"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "New access token generated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid or expired refresh token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/refresh-token")
    ApiResponse<String> refreshToken(@CookieValue(value = "refresh_token", defaultValue = "") String cookie) {
        return ApiResponse.<String>builder()
                .result(authenticationService.getNewAccessToken(cookie))
                .build();
    }
}
