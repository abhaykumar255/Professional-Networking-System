package com.professionalnetworking.apigateway.filters;

import com.professionalnetworking.apigateway.services.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            log.info("Logging request: {}", exchange.getRequest().getURI());

            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                log.info("Authorization token header not found");
                return handleUnauthorized(exchange, "Authorization token header not found");
            }

            String token = tokenHeader.substring(7);

            try {
                if (!jwtService.validateToken(token)) {
                    return handleUnauthorized(exchange, "Invalid token");
                }
                Long userId = jwtService.getUserIdFromToken(token);
                ServerWebExchange modifiedExchange = exchange
                        .mutate()
                        .request(r -> r.header("X-USER-ID", userId.toString())
                                .header("X-USER-ROLES", "USER"))
                        .build();

                return chain.filter(modifiedExchange);

            }catch (JwtException e){
                log.error("JWT validation failed: {}", e.getMessage());
                return handleUnauthorized(exchange, "Token validation failed");
                //return exchange.getResponse().setComplete();
            }
        };
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        log.warn("Unauthorized access attempt: {}", message);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String body = "{\"error\":\"Unauthorized\",\"message\":\"" + message + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    // This Class is used to configure the filter
    public static class Config {

    }
}
