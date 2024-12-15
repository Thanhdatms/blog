package com.group7.blog.services;

import com.group7.blog.dto.Auth.TokenCreation;
import com.group7.blog.dto.Auth.TokenResponse;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.reset-password-duration}")
    protected long RESETTABLE_DURATION;

    public TokenResponse generateToken(TokenCreation body) {
        // covert Java object to JSON strings
        // output: {"name":"mkyong","age":42}
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        String accessToken = signToken(jwsHeader, ACCESS_KEY, body, (int) VALID_DURATION, ChronoUnit.HOURS);
        String refreshToken = signToken(jwsHeader, REFRESH_KEY, body, (int) REFRESHABLE_DURATION, ChronoUnit.DAYS);
        return new TokenResponse(accessToken, refreshToken);
    }

    public String generateResetPasswordToken(TokenCreation body) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        return signToken(jwsHeader, ACCESS_KEY, body, (int) RESETTABLE_DURATION, ChronoUnit.MINUTES);
    }


    private String signToken (JWSHeader algo, String KEY, TokenCreation body, Integer duration, ChronoUnit type) {
        try {
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(body.getUserId().toString())
                    .issuer("group7.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(duration, type).toEpochMilli()
                    ))
                    .claim("username", body.getUsername())
                    .claim("roles", body.getRoles())
                    .build();
            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(algo, payload);
            jwsObject.sign(new MACSigner(KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = isRefresh ?
                new MACVerifier(REFRESH_KEY.getBytes()) :
                new MACVerifier(ACCESS_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.DAYS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

//        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
//            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
}
