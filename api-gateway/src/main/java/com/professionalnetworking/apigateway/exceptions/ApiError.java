package com.professionalnetworking.apigateway.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private String error;
    private String message;
    private HttpStatus statusCode;
    private int status;
    private String path;
    private String method;
    private String traceId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private Map<String, String> validationErrors;
    private List<String> details;

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

    public ApiError(String message, HttpStatus statusCode, String path) {
        this(message, statusCode);
        this.path = path;
    }

    public ApiError(String message, HttpStatus statusCode, String path, String method) {
        this(message, statusCode, path);
        this.method = method;
    }

    public ApiError(String message, HttpStatus statusCode, Map<String, String> validationErrors) {
        this(message, statusCode);
        this.validationErrors = validationErrors;
    }
}
