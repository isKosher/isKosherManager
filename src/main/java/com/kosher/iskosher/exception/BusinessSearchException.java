package com.kosher.iskosher.exception;

public class BusinessSearchException extends RuntimeException {
    public BusinessSearchException(String message) {
        super(message);
    }

    public BusinessSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}