package com.group7.blog.controllers;


import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        TokenResponse tokens = authenticationService.login(request);
        Cookie cookie = new Cookie("jwt", tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return ApiResponse.<String>builder()
                .result(tokens.getAccessToken())
                .build();
    }

//    @PostMapping("/login")
//    ApiResponse<IntrospecResponse> authenticate(@RequestBody IntrospecRequest request) throws ParseException, JOSEException {
//        var result   = authenticationService.introspect(request);
//        return ApiResponse.<IntrospecResponse>builder()
//                .result(result)
//                .build();
//    }

}
