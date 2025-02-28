package com.kosher.iskosher.types;


import lombok.Builder;

@Builder
public record TravelInfo (String drivingDuration, String drivingDistance, String walkingDuration, String walkingDistance){

}
