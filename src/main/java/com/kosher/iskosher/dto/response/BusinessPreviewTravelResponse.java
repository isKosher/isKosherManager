package com.kosher.iskosher.dto.response;

import com.kosher.iskosher.types.LocationDetails;
import com.kosher.iskosher.types.TravelInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessPreviewTravelResponse extends BusinessPreviewResponse {
    private TravelInfo travelInfo;

    public BusinessPreviewTravelResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                         String address, Integer streetNumber, String city, Double longitude,
                                         Double latitude, String businessPhotos, String kosherType, String businessType) {
        super(businessId, businessName, foodTypes, foodItemTypes, businessPhotos, kosherType, businessType);
        setLocation(new LocationDetails(address, streetNumber, city, "", latitude, longitude));
    }

}
