package com.kosher.iskosher.exception;

import java.util.UUID;

public class BusinessNotFoundException extends RuntimeException {
    private final UUID businessId;

    public BusinessNotFoundException(UUID businessId) {
        super(String.format("Business with ID %s not found", businessId));
        this.businessId = businessId;
    }

    public UUID getBusinessId() {
        return businessId;
    }
}

