package com.kosher.iskosher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationModel {
    private String addressName;
    private String apartmentNumber;
    private String cityName;
    private String country;
    private String region;
    private String additionalNotes;
    public LocationModel(String addressName, String apartmentNumber, String cityName, String country) {
        this.addressName = addressName;
        this.apartmentNumber = apartmentNumber;
        this.cityName = cityName;
        this.country = country;
        this.additionalNotes = null;
    }
}
