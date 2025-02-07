package com.kosher.iskosher.dto.response;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.dto.KosherTypeDto;
import com.kosher.iskosher.types.LocationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

import static com.kosher.iskosher.common.utils.JsonParserUtil.*;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessPreviewResponse {
    private UUID businessId;
    private String businessName;
    private String foodTypes;
    private String foodItemTypes;
    private LocationInfo location;
    private String businessPhotos;
    private String kosherTypes;
    private String businessType;

    public BusinessPreviewResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                   String address, Integer streetNumber, String city, String businessPhotos,
                                   String kosherTypes, String business_type) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.foodTypes = foodTypes;
        this.foodItemTypes = foodItemTypes;
        this.setLocation(new LocationInfo(address, streetNumber, city));
        this.businessPhotos = businessPhotos;
        this.kosherTypes = kosherTypes;
        this.businessType = business_type;
    }

    public BusinessPreviewResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                   String businessPhotos, String kosherType, String businessType) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.foodTypes = foodTypes;
        this.foodItemTypes = foodItemTypes;
        this.businessPhotos = businessPhotos;
        this.kosherTypes = kosherType;
        this.businessType = businessType;
    }

    public List<String> getFoodTypes() {
        return parseJson(foodTypes, String.class);
    }

    public List<String> getFoodItemTypes() {
        return parseJson(foodItemTypes, String.class);
    }

    public List<KosherTypeDto> getKosherTypes(){
        return parseJson(kosherTypes, KosherTypeDto.class);
    }
    public List<BusinessPhotoDto> getBusinessPhotos() {
        return parseJson(businessPhotos, BusinessPhotoDto.class);
    }

}
