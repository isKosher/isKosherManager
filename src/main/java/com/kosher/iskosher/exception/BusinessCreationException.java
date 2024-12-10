package com.kosher.iskosher.exception;

public class BusinessCreationException extends RuntimeException {

    private final String businessName;

    public BusinessCreationException(String message) {
        super(message);
        this.businessName = null;
    }

    public BusinessCreationException(String message, Throwable cause) {
        super(message, cause);
        this.businessName = null;
    }

    public BusinessCreationException(String message, String businessName, Throwable cause) {
        super(message, cause);
        this.businessName = businessName;
    }

    public String getBusinessName() {
        return businessName;
    }

    @Override
    public String toString() {
        return "BusinessCreationException{" +
                "message=" + getMessage() +
                ", businessName='" + businessName + '\'' +
                ", cause=" + getCause() +
                '}';
    }
}
