package com.professionalnetworking.apigateway.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception exception) {
        ApiError apiError =  new ApiError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException exception) {
        ApiError apiError =  new ApiError(exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }
}
