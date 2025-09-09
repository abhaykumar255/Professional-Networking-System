package com.professionalnetworking.notificationservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private String error;
    private String message;
    private HttpStatus statusCode;
    private int status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private Map<String, String> validationErrors;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String message, HttpStatus statusCode) {
        this();
        this.message = message;
        this.error = statusCode.getReasonPhrase();
        this.statusCode = statusCode;
        this.status = statusCode.value();
    }

    public ApiError(String message, HttpStatus statusCode, Map<String, String> validationErrors) {
        this(message, statusCode);
        this.validationErrors = validationErrors;
    }
}