package com.kosher.iskosher.dto.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessPreviewResponse {
    private UUID businessId;
    private String businessName;
    private String foodTypes;
    private String foodItemTypes;
    private String address;
    private Integer streetNumber;
    private String city;
    private String businessPhotos;
    private String kosherType;
    private String businessType;
    public BusinessPreviewResponse(UUID business_id, String business_name, String food_types, String food_item_types,
                                   String address, Integer street_number, String city, String business_photos,
                                   String kosher_type, String business_type) {
        this.businessId = business_id;
        this.businessName = business_name;
        this.foodTypes = food_types;
        this.foodItemTypes = food_item_types;
        this.address = address;
        this.streetNumber = street_number;
        this.city = city;
        this.businessPhotos = business_photos;
        this.kosherType = kosher_type;
        this.businessType = business_type;
    }

    public List<String> getFoodTypes() {
        return parseJsonArray(foodTypes);
    }

    public List<String> getFoodItemTypes() {
        return parseJsonArray(foodItemTypes);
    }

    public List<String> getBusinessPhotos() {
        return parseJsonArray(businessPhotos);
    }

    private List<String> parseJsonArray(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
