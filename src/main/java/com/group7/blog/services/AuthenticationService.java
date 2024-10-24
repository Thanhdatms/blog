package com.group7.blog.services;
import com.group7.blog.dto.Auth.LoginRequest;
import com.group7.blog.dto.Auth.TokenCreation;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    TokenService tokenService;

    public TokenResponse login(LoginRequest request){
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        System.out.println(user);
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
}
