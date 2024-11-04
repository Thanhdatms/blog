package com.group7.blog.configurations;

import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.services.TokenService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJWTDecoder implements JwtDecoder {
    @Value("${jwt.access-key}")
    private String accessKey;

    @Autowired
    private TokenService tokenService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws BadJwtException {
        try {
            try {
                tokenService.verifyToken(token, false);
            } catch (AppException e) {
                throw new BadJwtException("Invalid toke");
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
