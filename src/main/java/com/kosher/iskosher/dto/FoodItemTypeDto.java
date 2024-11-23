package com.kosher.iskosher.dto;

import com.kosher.iskosher.common.interfaces.NamedEntityDto;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record FoodItemTypeDto(UUID id, String name) implements Serializable, NamedEntityDto {
}