package com.kosher.iskosher.dto.request;

import com.kosher.iskosher.configuration.ManagedBusiness;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BusinessUpdateRequest(
        @NotNull(message = "Business ID is required")
        @ManagedBusiness
        UUID businessId,
        String businessName,
        String businessPhone,
        String businessDetails,
        Short businessRating,
        List<String> kosherTypes,
        String businessType,
        List<String> foodTypes,
        List<String> foodItemTypes
) {
}
