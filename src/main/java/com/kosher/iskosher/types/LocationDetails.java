package com.kosher.iskosher.types;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDetails extends LocationInfo {
    private String locationDetails;
    private double latitude;
    private double longitude;

    public LocationDetails(String address, Integer streetNumber, String city, String locationDetails, double latitude
            , double longitude) {
        super(address, streetNumber, city);
        this.locationDetails = locationDetails;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
