package com.kosher.iskosher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record LocationDto(
        @NotNull(message = "Street number is required") Integer streetNumber,
        @NotBlank(message = "Address name is required")
        @Size(max = 255, message = "Address name must be at most 255 characters") String address,
        @NotBlank(message = "City name is required")
        @Size(max = 100, message = "City name must be at most 100 characters") String city,
        String region, Double longitude, Double latitude,
        @NotBlank(message = "Details are required")
        @Size(max = 1000, message = "Details must be at most 1000 characters") String locationDetails
) implements Serializable {
}
