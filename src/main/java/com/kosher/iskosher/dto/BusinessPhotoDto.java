package com.kosher.iskosher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.util.UUID;

public record BusinessPhotoDto(
        UUID id,
        @NotNull @URL String url,
        @JsonProperty("photo_info")
        String photoInfo
) implements Serializable {
}
