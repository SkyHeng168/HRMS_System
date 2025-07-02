package com.cotech.systemcoreapi.exception.CustomException;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
