package com.kosher.iskosher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosher.iskosher.common.interfaces.NamedEntityDto;

import java.io.Serializable;
import java.util.UUID;

public record KosherTypeDto(
        UUID id, String name,
        @JsonProperty("kosher_icon_url") String kosherIconUrl) implements Serializable, NamedEntityDto {
}