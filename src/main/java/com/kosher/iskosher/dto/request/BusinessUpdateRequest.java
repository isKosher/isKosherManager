package com.kosher.iskosher.dto.request;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.dto.LocationDto;
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
        LocationDto location,
        String kosherTypeName,
        String businessTypeName,
        List<String> foodTypes,
        List<String> foodItemTypes,
        List<BusinessPhotoDto> businessPhotos,
        KosherSupervisorDto supervisor,
        KosherCertificateDto kosherCertificate
) {
}
