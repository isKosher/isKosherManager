package com.kosher.iskosher.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String refreshTokenNotFound) {
        super(refreshTokenNotFound);
    }
}

