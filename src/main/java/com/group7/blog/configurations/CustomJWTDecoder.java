package com.group7.blog.configurations;

import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.services.TokenService;
import com.group7.blog.services.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomJWTDecoder implements JwtDecoder {
    @Value("${jwt.access-key}")
    private String accessKey;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws BadJwtException {
        try {
            try {
                SignedJWT result = tokenService.verifyToken(token, false);
                if(!userService.checkUserExistById(result.getJWTClaimsSet().getSubject())) {
                    throw new AppException(ErrorCode.USER_NOT_EXISTED);
                }
            } catch (AppException e) {
                throw new BadJwtException("Invalid token");
            }
            if(Objects.isNull(nimbusJwtDecoder)){
                SecretKeySpec secretKeySpec = new SecretKeySpec(accessKey.getBytes(), "HS512");
                nimbusJwtDecoder = NimbusJwtDecoder
                        .withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build();
            }
            return nimbusJwtDecoder.decode(token);
        } catch (ParseException | JOSEException e) {
            throw new BadJwtException(e.getMessage());
        }
    }
}
