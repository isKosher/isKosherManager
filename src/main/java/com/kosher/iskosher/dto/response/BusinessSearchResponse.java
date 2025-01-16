package com.kosher.iskosher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class BusinessSearchResponse {
    private UUID businessId;
    private String businessName;
    private String address;
    private String city;
    private Float matchScore;

    public BusinessSearchResponse(Object[] tuple) {
        this.businessId = UUID.fromString(tuple[0].toString());
        this.businessName = (String) tuple[1];
        this.address = (String) tuple[2];
        this.city = (String) tuple[3];
        this.matchScore = tuple[4] == null ? null : ((Number) tuple[4]).floatValue();
    }
}
