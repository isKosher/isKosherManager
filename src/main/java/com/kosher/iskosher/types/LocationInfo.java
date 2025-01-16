package com.kosher.iskosher.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationInfo {
    private String address;
    private Integer streetNumber;
    private String city;

}
