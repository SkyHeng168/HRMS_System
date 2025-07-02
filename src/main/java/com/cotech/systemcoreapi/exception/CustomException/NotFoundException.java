package com.cotech.systemcoreapi.exception.CustomException;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
