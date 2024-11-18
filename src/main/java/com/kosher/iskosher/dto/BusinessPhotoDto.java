package com.kosher.iskosher.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
public record BusinessPhotoDto(
        @NotNull @URL String url,
        @JsonProperty("photo_info")
        String photoInfo
) implements Serializable {
}
