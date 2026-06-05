package com.example.dailydictation.config;

import com.example.dailydictation.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
@Component
public class Jwt {

    @NonFinal
    protected static final String SIGN_KEY =
            "A7IHNV7cpx/zw7pnj/O/W3QxSTJF+3Uy+ld4l5U7zvHTfoaXEEipRVUMUwkLx5aQ";


    public String generateToken(User user) {
//            if u wanna create token . u need to create HEADER and PAYLOAD
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("william")
                .issueTime(new Date())
                // Token expiration time
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("scope", buildScopeUser(user))
                .build();
        //PAYLAOD will receive a jwtClaimsSet.toJSONObject()
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScopeUser(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(eRole -> stringJoiner.add(eRole.name()));
        return stringJoiner.toString();
    }

}
