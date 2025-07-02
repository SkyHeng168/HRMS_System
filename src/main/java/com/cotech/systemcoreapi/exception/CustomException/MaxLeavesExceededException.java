package com.cotech.systemcoreapi.exception.CustomException;

public class MaxLeavesExceededException extends RuntimeException {
    public MaxLeavesExceededException(String message) {
        super(message);
    }
}