package com.kosher.iskosher.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.kosher.iskosher.entity.City}
 */
public record CityDto(UUID id, String name) implements Serializable {
}