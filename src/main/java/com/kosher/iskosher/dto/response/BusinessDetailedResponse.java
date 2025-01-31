package com.kosher.iskosher.dto.response;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.types.LocationDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

import static com.kosher.iskosher.common.utils.JsonParserUtil.parseJson;
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessDetailedResponse extends BusinessPreviewResponse {

    private String businessDetails;
    private LocationDetails location;
    private Short businessRating;
    private String kosherSupervisors;
    private String kosherCertificates;


    public BusinessDetailedResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                    String address, Integer streetNumber, String city, Double longitude,
                                    Double latitude, String businessPhotos, String kosherType, String businessType,
                                    String businessDetails, String locationDetails, Short businessRating,
                                    String kosherSupervisors, String kosherCertificates) {
        super(businessId, businessName, foodTypes, foodItemTypes, businessPhotos, kosherType, businessType);
        this.businessDetails = businessDetails;
        setLocation(new LocationDetails(address, streetNumber, city, locationDetails, latitude, longitude));
        this.businessRating = businessRating;
        this.kosherSupervisors = kosherSupervisors;
        this.kosherCertificates = kosherCertificates;
    }

    public List<KosherSupervisorDto> getKosherSupervisors() {
        return parseJson(kosherSupervisors, KosherSupervisorDto.class);
    }

    public List<KosherCertificateDto> getKosherCertificates() {
        return parseJson(kosherCertificates, KosherCertificateDto.class);
    }
}

