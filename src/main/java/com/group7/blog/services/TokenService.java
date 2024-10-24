package com.group7.blog.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group7.blog.dto.Auth.TokenCreation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.group7.blog.dto.Auth.TokenResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenService {
    @NonFinal
    @Value("${jwt.access-key}")
    private String ACCESS_KEY;
    @NonFinal
    @Value("${jwt.refresh-key}")
    private String REFRESH_KEY;

    public TokenResponse generateToken(TokenCreation body) {
        // covert Java object to JSON strings
        // output: {"name":"mkyong","age":42}
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        String accessToken = signToken(jwsHeader, ACCESS_KEY, body, 5);
        String refreshToken = signToken(jwsHeader, REFRESH_KEY, body, 15);
        return new TokenResponse(accessToken, refreshToken);
    }

    private String signToken (JWSHeader algo, String KEY, TokenCreation body, Integer days) {
        try {
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(body.getUserId().toString())
                    .issuer("group7.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(days, ChronoUnit.DAYS).toEpochMilli()
                    ))
                    .claim("username", body.getUsername())
                    .build();
            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(algo, payload);
            jwsObject.sign(new MACSigner(KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
