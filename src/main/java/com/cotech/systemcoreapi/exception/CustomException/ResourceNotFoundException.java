package com.cotech.systemcoreapi.exception.CustomException;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource) {
        super(resource + " not found.");
    }
}
