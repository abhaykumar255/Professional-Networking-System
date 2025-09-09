package com.professionalnetworking.connectionservice.exception.custom_exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
