package com.kosher.iskosher.dto;

import com.kosher.iskosher.common.interfaces.NamedEntityDto;

import java.io.Serializable;
import java.util.UUID;

public record BusinessTypeDto(UUID id, String name) implements Serializable, NamedEntityDto {
}