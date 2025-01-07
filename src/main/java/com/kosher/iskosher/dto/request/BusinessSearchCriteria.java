package com.kosher.iskosher.dto.request;

import lombok.Data;

@Data
public class BusinessSearchCriteria {
    private String businessType;
    private String foodType;
    private String foodItemType;
    private String city;
    private String kosherType;
    private Integer rating;
}