package com.kosher.iskosher.dto.response;

import com.kosher.iskosher.types.DestinationLocation;
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
    private DestinationLocation destLoc;

    public BusinessPreviewTravelResponse(UUID businessId, String businessName, String foodTypes,
                                         String foodItemTypes, String address, Integer streetNumber, String city,
                                         String businessPhotos, String kosherTypes, String business_type,
                                         TravelInfo travelInfo,
                                         DestinationLocation destLoc) {
        super(businessId, businessName, foodTypes, foodItemTypes, address, streetNumber, city, businessPhotos, kosherTypes, business_type);
        this.travelInfo = travelInfo;
        this.destLoc = destLoc;
    }

}
