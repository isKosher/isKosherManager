package com.kosher.iskosher.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BusinessFilterCriteria {
    private List<String> businessTypes;
    private List<String> foodTypes;
    private List<String> foodItemTypes;
    private String city;
    private List<String> kosherTypes;
    private Integer rating;
}