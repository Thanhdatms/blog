package com.group7.blog.services;
import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenCreation;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.UserRepository;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.UUID;

import static com.cloudinary.AccessControlRule.AccessType.token;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    TokenService tokenService;
    UserService userService;


    @NonFinal
    @Value("${server.cookie.domain}")
    private String domain;

    @NonFinal
    @Value("${server.cookie.path}")
    private String path;


    public TokenResponse login(LoginRequest request){
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getHashpassword());
        if(!isMatched){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        TokenResponse tokens = tokenService.generateToken(new TokenCreation(user.getId(), user.getUsername()));
        user.setRefreshtoken(tokens.getRefreshToken());
        userRepository.save(user);
        return tokens;
    }

    public TokenResponse refreshToken(String token) {
        try {
            SignedJWT result = tokenService.verifyToken(token, true);
            Users user = userRepository
                    .findById(UUID.fromString(result.getJWTClaimsSet().getSubject()))
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            return tokenService.generateToken(new TokenCreation(user.getId(), user.getUsername()));
        } catch (AppException | ParseException | JOSEException e) {
            throw new BadJwtException("Invalid token");
        }
    }

    public Cookie getCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setDomain(domain);
        return cookie;
    }
}
