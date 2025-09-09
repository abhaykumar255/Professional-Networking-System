package com.professionalnetworking.apigateway.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

@Component
@Order(-1)
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Global exception occurred: ", ex);

        ApiError apiError = createApiError(ex);

        exchange.getResponse().setStatusCode(apiError.getStatusCode());
        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        try {
            String errorJson = objectMapper.writeValueAsString(apiError);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorJson.getBytes());
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error serializing error response", e);
            return exchange.getResponse().setComplete();
        }
    }

    private ApiError createApiError(Throwable ex) {
        if (ex instanceof JwtException) {
            return new ApiError("Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
        } else if (ex instanceof AccessDeniedException) {
            return new ApiError("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
        } else if (ex instanceof NotFoundException) {
            return new ApiError("Service not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
        } else if (ex instanceof ResponseStatusException rse) {
            return new ApiError(rse.getReason(), HttpStatus.valueOf(rse.getStatusCode().value()));
        } else if (ex instanceof IllegalArgumentException) {
            return new ApiError("Invalid request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        } else {
            return new ApiError("Internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
