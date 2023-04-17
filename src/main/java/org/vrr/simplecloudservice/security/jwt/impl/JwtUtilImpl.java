package org.vrr.simplecloudservice.security.jwt.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.vrr.simplecloudservice.properties.JwtProperties;
import org.vrr.simplecloudservice.security.jwt.JwtUtil;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtilImpl implements JwtUtil {

    private final static String SUBJECT = "User details";

    private final static String UUID_CLAIM_NAME = "uuid";

    private final JwtProperties jwtProperties;

    @Override
    public String generateToken(String uuid) {
        Date accessExpiration = Date.from(ZonedDateTime.now()
                .plusMinutes(jwtProperties.getTokenValidity()).toInstant());
        return JWT.create()
                .withSubject(SUBJECT)
                .withClaim(UUID_CLAIM_NAME, uuid)
                .withIssuedAt(new Date())
                .withIssuer(jwtProperties.getIssuer())
                .withExpiresAt(accessExpiration)
                .sign(Algorithm.HMAC256(jwtProperties.getTokenSecret()));
    }

    @Override
    public String validateTokenAndRetrieveClaim(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.getTokenSecret()))
                .withSubject(SUBJECT)
                .withIssuer(jwtProperties.getIssuer())
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(UUID_CLAIM_NAME).asString();
    }

    public String getUuidFromToken(String token){
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(UUID_CLAIM_NAME).asString();
    }
}
