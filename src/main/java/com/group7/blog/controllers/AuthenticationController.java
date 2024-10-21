package com.group7.blog.controllers;


import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.User.reponse.AuthenticationResponse;
import com.group7.blog.dto.User.reponse.IntrospecResponse;
import com.group7.blog.dto.User.request.AuthenticationRequest;
import com.group7.blog.dto.User.request.IntrospecRequest;
import com.group7.blog.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result  = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospecResponse> authenticate(@RequestBody IntrospecRequest request) throws ParseException, JOSEException {
        var result   = authenticationService.introspect(request);
        return ApiResponse.<IntrospecResponse>builder()
                .result(result)
                .build();
    }

}
