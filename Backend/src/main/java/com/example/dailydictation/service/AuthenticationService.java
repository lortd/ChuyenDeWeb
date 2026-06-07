package com.example.dailydictation.service;

import com.example.dailydictation.dto.request.AuthenticationRequest;
import com.example.dailydictation.dto.request.IntrospectRequest;
import com.example.dailydictation.dto.response.AuthenticationResponse;
import com.example.dailydictation.dto.response.IntrospectResponse;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.StringJoiner;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @NonFinal
    protected static final String SIGN_KEY =
            "A7IHNV7cpx/zw7pnj/O/W3QxSTJF+3Uy+ld4l5U7zvHTfoaXEEipRVUMUwkLx5aQ";


    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var token = introspectRequest.getToken();
        JWSVerifier jwsVerifier = new MACVerifier(SIGN_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(jwsVerifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String input = authenticationRequest.getUserName(); // Đây có thể là username hoặc email

        // Tìm theo username hoặc email
        User user = userRepository.findByUserName(input)
                .or(() -> userRepository.findByGmail(input)) // nếu không phải username thì thử là email
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra đã verify chưa
        if (!user.isEnabled()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        // Kiểm tra mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new RuntimeException("Invalid credentials");
        }

        // Sinh token
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .nickName(user.getNickName())
                .userId(user.getId())
                .build();
    }


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
