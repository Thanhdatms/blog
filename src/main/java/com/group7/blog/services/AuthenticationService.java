package com.group7.blog.services;
import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenCreation;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.dto.History.request.HistoryCreation;
import com.group7.blog.enums.EnumData;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.mappers.RoleMapper;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    TokenService tokenService;
    HistoryService historyService;
    EmailService emailService;
    RoleMapper roleMapper;

    @NonFinal
    @Value("${server.cookie.domain}")
    private String domain;

    @NonFinal
    @Value("${server.cookie.path}")
    private String path;

    public TokenResponse login(LoginRequest request){
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(user.getIsLock() != null && user.getIsLock().equals(true)) {
            if(user.getLastLoginFailed().before(Timestamp.valueOf(LocalDateTime.now().minusMinutes(15)))) {
                user.setIsLock(false);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("username", user.getUsername());
                model.put("email", user.getEmail());
                model.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                emailService.sendAdminAlert(
                        "beplodao@gmail.com",
                        "Security Alert: Failed Login Attempts",
                        model,
                        "admin-email-alert-template"
                );
                throw new AppException(ErrorCode.IS_LOCKED);
            }
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getHashPassword());
        if(!isMatched){

            HistoryCreation historyCreation = new HistoryCreation();
            historyCreation.setEmail(user.getEmail());
            historyCreation.setUsers(user);
            historyCreation.setModel(Users.class.getSimpleName());
            historyCreation.setActionType(EnumData.HistoryActionType.LOGIN);
            historyCreation.setActionStatus(EnumData.HistoryActionStatus.FAILED);
            historyService.createHistory(historyCreation);

            user.setLoginFailedCount(
                    user.getLoginFailedCount() + 1
            );


            if(user.getLoginFailedCount() == 5) {
                user.setIsLock(true);
                user.setLastLoginFailed(Timestamp.valueOf(LocalDateTime.now()));
            }

            userRepository.save(user);

            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        TokenResponse tokens = tokenService.generateToken(new TokenCreation(
                user.getId(),
                user.getUsername(),
                user.getUserRoles().stream().map(
                        item -> roleMapper.toRoleResponse(item.getRole())
                ).toList())
        );

        user.setRefreshToken(tokens.getRefreshToken());
        user.setLoginFailedCount(0);
        userRepository.save(user);
        return tokens;
    }

//    public IntrospecResponse introspect(IntrospecRequest request) throws JOSEException, ParseException {
//        var token = request.getToken();
//
//        // Create verifier object with SIGNER_KEY => Covert this to byte for calculation
//        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
//
//
//        SignedJWT signedJWT = SignedJWT.parse(token);
//
//        // Check expiry data
//        Date expiryDate = signedJWT.getJWTClaimsSet().getExpirationTime();
//
//        // verify token => re
//        var verify = signedJWT.verify(verifier);
//
//        return IntrospecResponse.builder()
//                .valid(verify && expiryDate.after(new Date()))
//                .build();
//    }

    public Cookie getCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60 * 24 * 1000);
        return cookie;
    }
}
