package com.kosher.iskosher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessDetailedResponse {
    private UUID businessId;
    private String businessName;
    private List<String> foodTypes;
    private List<String> foodItemTypes;
    private String address;
    private Integer streetNumber;
    private String city;
    private List<String> businessPhotos;
    private String kosherType;
    private String businessType;
}