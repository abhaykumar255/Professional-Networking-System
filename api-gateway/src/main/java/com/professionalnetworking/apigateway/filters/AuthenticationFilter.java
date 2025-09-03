package com.professionalnetworking.apigateway.filters;

import com.professionalnetworking.apigateway.services.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

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
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.info("Authorization token header not found");
                return exchange.getResponse().setComplete();
            }

            String token = tokenHeader.substring(7);

            try {
                Long userId = jwtService.getUserIdFromToken(token);
                ServerWebExchange serverWebExchange = exchange
                        .mutate()
                        .request(r -> r.header("X-USER-ID", userId.toString()))
                        .build();

                return chain.filter(serverWebExchange);

            }catch (JwtException e){
                log.error("Invalid token: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

        };
    }

    // This Class is used to configure the filter
    public static class Config {

    }
}
