package com.kosher.iskosher.dto.request;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.dto.KosherSupervisorDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BusinessCreateRequest(
        @NotNull(message = "Business name is required")
        String businessName,

        @NotNull(message = "Business phone is required")
        String businessPhone,
        @NotNull(message = "Business details are required")
        String businessDetails,
        @Min(value = 1, message = "Business rating must be at least 1")
        @Max(value = 5, message = "Business rating cannot exceed 5")
        Short businessRating,
        @NotNull(message = "Address name is required")
        String addressName,
        @NotNull(message = "Street number is required")
        Integer streetNumber,
        @NotNull(message = "Region name is required")
        String region,
        String locationDetails,
        @NotNull(message = "City name is required")
        String cityName,
        String kosherTypeName,
        String businessTypeName,
        @NotNull(message = "Food types are required")
        List<String> foodTypes,
        List<String> foodItemTypes,
        List<BusinessPhotoDto> businessPhotos,
        KosherSupervisorDto supervisor,
        KosherCertificateDto kosherCertificate
) {

}
