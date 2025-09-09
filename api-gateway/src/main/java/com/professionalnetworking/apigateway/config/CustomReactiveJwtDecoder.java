package com.professionalnetworking.apigateway.config;

import com.professionalnetworking.apigateway.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class CustomReactiveJwtDecoder implements ReactiveJwtDecoder {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        try {
            Long userId = jwtService.getUserIdFromToken(token);

            return Mono.just(Jwt.withTokenValue(token)
                    .header("alg", "HS256")
                    .header("typ", "JWT")
                    .claim("sub", userId.toString())
                    .claim("userId", userId)
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(7200))
                    .build());
        } catch (Exception e) {
            return Mono.error(new JwtException("Invalid JWT token", e));
        }
    }
}
