package com.kosher.iskosher.exception;

import com.kosher.iskosher.common.enums.ErrorType;

public class BusinessFilterException extends RuntimeException {
    private final ErrorType errorType;

    public BusinessFilterException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public BusinessFilterException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
