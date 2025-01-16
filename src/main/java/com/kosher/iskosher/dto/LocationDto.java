package com.kosher.iskosher.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record LocationDto(@NotNull Integer streetNumber, @NotNull String address, @NotNull String city,
                          @NotNull String region, Double longitude, Double latitude,
                          @NotNull String locationDetails) implements Serializable {
}