package com.kosher.iskosher.dto.response;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.dto.KosherTypeDto;
import com.kosher.iskosher.types.LocationDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOwnedBusinessResponse {
    private UUID businessId;
    private String businessName;
    private String businessDetails;
    private Short businessRating;
    private String businessNumber;
    private List<KosherTypeDto> kosherTypes;
    private String businessType;
    private LocationDetails location;
    private List<KosherSupervisorDto> supervisors;
    private List<KosherCertificateDto> certificates;
    private List<String> foodTypes;
    private List<String> foodItemTypes;
    private List<BusinessPhotoDto> businessPhotos;
}

