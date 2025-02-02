package com.kosher.iskosher.exception;

//TOD0: add to custom exception
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}