package com.kosher.iskosher.dto.request;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.dto.LocationDto;
import jakarta.validation.constraints.*;

import java.util.List;

public record BusinessCreateRequest(
        @NotBlank(message = "Business name is required")
        String businessName,

        @NotBlank(message = "Business phone is required")
        String businessPhone,

        @NotBlank(message = "Business details are required")
        String businessDetails,

//        @Min(value = 1, message = "Business rating must be at least 1")
//        @Max(value = 5, message = "Business rating cannot exceed 5")
//        @NotNull(message = "Business rating is required")
        Short businessRating,

        @NotNull(message = "Location information is required")
        LocationDto location,

        @NotEmpty(message = "Kosher types are required")
        List<String> kosherTypes,

        @NotBlank(message = "Business type name is required")
        String businessTypeName,

        @NotEmpty(message = "Food types are required")
        List<String> foodTypes,

        @NotEmpty(message = "Food item types are required")
        List<String> foodItemTypes,

        //@NotEmpty(message = "Business photos are required")
        List<BusinessPhotoDto> businessPhotos,

        @NotNull(message = "Kosher supervisor is required")
        KosherSupervisorDto supervisor,

        @NotNull(message = "Kosher certificate is required")
        KosherCertificateDto kosherCertificate
) {
}
