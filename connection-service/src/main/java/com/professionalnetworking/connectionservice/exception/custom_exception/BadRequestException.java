package com.professionalnetworking.connectionservice.exception.custom_exception;


public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
