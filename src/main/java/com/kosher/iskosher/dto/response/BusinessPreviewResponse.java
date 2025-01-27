package com.kosher.iskosher.dto.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.types.LocationInfo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessPreviewResponse {
    private UUID businessId;
    private String businessName;
    private String foodTypes;
    private String foodItemTypes;
    private LocationInfo location;
    private String businessPhotos;
    private String kosherType;
    private String businessType;

    public BusinessPreviewResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                   String address, Integer streetNumber, String city, String businessPhotos,
                                   String kosherType, String business_type) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.foodTypes = foodTypes;
        this.foodItemTypes = foodItemTypes;
        this.setLocation(new LocationInfo(address, streetNumber, city));
        this.businessPhotos = businessPhotos;
        this.kosherType = kosherType;
        this.businessType = business_type;
    }

    public BusinessPreviewResponse(UUID businessId, String businessName, String foodTypes, String foodItemTypes,
                                   String businessPhotos, String kosherType, String businessType) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.foodTypes = foodTypes;
        this.foodItemTypes = foodItemTypes;
        this.businessPhotos = businessPhotos;
        this.kosherType = kosherType;
        this.businessType = businessType;
    }

    public List<String> getFoodTypes() {
        return parseJsonArray(foodTypes);
    }

    public List<String> getFoodItemTypes() {
        return parseJsonArray(foodItemTypes);
    }


    public List<BusinessPhotoDto> getBusinessPhotos() {
        return parseJsonToBusinessPhotos(businessPhotos);
    }


    private List<BusinessPhotoDto> parseJsonToBusinessPhotos(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<BusinessPhotoDto>>() {
            });
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<String> parseJsonArray(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
