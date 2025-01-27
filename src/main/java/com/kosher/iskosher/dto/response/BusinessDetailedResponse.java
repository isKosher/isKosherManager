package com.kosher.iskosher.dto.response;

import com.kosher.iskosher.types.LocationDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;


@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessDetailedResponse extends BusinessPreviewResponse {

    private String businessDetails;
    private LocationDetails location;
    private Short businessRating;
    private String supervisorName;
    private String supervisorContact;
    private String supervisorAuthority;
    private String businessCertificate;
    private LocalDate expirationDate;


    public BusinessDetailedResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                    String address, Integer streetNumber, String city, Double longitude,
                                    Double latitude, String businessPhotos, String kosherType, String businessType,
                                    String businessDetails, String locationDetails, Short businessRating,
                                    String supervisorName, String supervisorContact, String supervisorAuthority,
                                    String businessCertificate, LocalDate expirationDate) {
        super(businessId, businessName, foodTypes, foodItemTypes, businessPhotos, kosherType, businessType);
        this.businessDetails = businessDetails;
        setLocation(new LocationDetails(address, streetNumber, city, locationDetails, latitude, longitude));
        this.businessRating = businessRating;
        this.supervisorName = supervisorName;
        this.supervisorContact = supervisorContact;
        this.supervisorAuthority = supervisorAuthority;
        this.businessCertificate = businessCertificate;
        this.expirationDate = expirationDate;
    }

}

