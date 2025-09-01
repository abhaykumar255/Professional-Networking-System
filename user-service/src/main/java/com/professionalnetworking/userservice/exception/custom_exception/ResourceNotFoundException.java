package com.professionalnetworking.userservice.exception.custom_exception;

public class ResourceNotFoundException extends  RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
