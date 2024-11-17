package com.kosher.iskosher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessDetailedResponse extends BusinessPreviewResponse {

    private String businessDetails;
    private String locationDetails;
    private Short businessRating;
    private String supervisorName;
    private String supervisorContact;
    private String supervisorAuthority;
    private String businessCertificate;
    private LocalDate expirationDate;


    public BusinessDetailedResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                    String address, Integer streetNumber, String city, String businessPhotos,
                                    String kosherType, String business_type, String businessDetails,
                                    String locationDetails, Short businessRating, String supervisorName,
                                    String supervisorContact, String supervisorAuthority, String businessCertificate,
                                    LocalDate expirationDate) {
        super(businessId, businessName, foodTypes, foodItemTypes, address, streetNumber, city, businessPhotos,
                kosherType, business_type);
        this.businessDetails = businessDetails;
        this.locationDetails = locationDetails;
        this.businessRating = businessRating;
        this.supervisorName = supervisorName;
        this.supervisorContact = supervisorContact;
        this.supervisorAuthority = supervisorAuthority;
        this.businessCertificate = businessCertificate;
        this.expirationDate = expirationDate;
    }
}