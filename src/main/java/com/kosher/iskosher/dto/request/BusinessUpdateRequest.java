package com.kosher.iskosher.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BusinessUpdateRequest(
        @NotNull(message = "Business ID is required")
        UUID businessId,
        String businessName,
        String businessPhone,
        String businessDetails,
        Short businessRating,
        String kosherType,
        String businessType,
        List<String> foodTypes,
        List<String> foodItemTypes
) {
}
