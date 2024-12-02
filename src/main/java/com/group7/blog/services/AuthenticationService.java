package com.group7.blog.services;
import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenCreation;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    TokenService tokenService;

    @NonFinal
    @Value("${server.cookie.sameSite}")
    private String sameSite;

    @NonFinal
    @Value("${server.cookie.secure}")
    private String secure;

    public TokenResponse login(LoginRequest request){
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getHashPassword());
        if(!isMatched){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        TokenResponse tokens = tokenService.generateToken(new TokenCreation(user.getId(), user.getUsername()));
        user.setRefreshToken(tokens.getRefreshToken());
        userRepository.save(user);
        return tokens;
    }

    public ResponseCookie getCookie(String name, String value) {
        return ResponseCookie.from(name, value)// key & value
                .httpOnly(true)
                .secure(secure.equals("true"))
                .path("/")
                .maxAge(36000)
                .build();
    }

    public ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "") // key & value
                .httpOnly(true)
                .secure(secure.equals("true"))
                .path("/")
                .maxAge(0)
                .build();
    }

    public String logOut(String refreshToken) {
        try{
            if(refreshToken == null || refreshToken.isEmpty()) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            SignedJWT result = tokenService.verifyToken(refreshToken, true);
            Users user = userRepository.findById(UUID.fromString(result.getJWTClaimsSet().getSubject()))
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));
            if(userRepository.findOneByRefreshToken(refreshToken).isEmpty()) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            user.setRefreshToken(null);
            userRepository.save(user);
            return "Logout Successfully!";
        } catch ( ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

    }

    public String getNewAccessToken(String refreshToken) {
        try{
            if(refreshToken == null || refreshToken.isEmpty()) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            SignedJWT result = tokenService.verifyToken(refreshToken, true);
            Users user = userRepository.findById(UUID.fromString(result.getJWTClaimsSet().getSubject()))
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));
            if(userRepository.findOneByRefreshToken(refreshToken).isEmpty()) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            TokenResponse tokens = tokenService.generateToken(new TokenCreation(user.getId(), user.getUsername()));
            return tokens.getAccessToken();
        } catch ( ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

    }
}
