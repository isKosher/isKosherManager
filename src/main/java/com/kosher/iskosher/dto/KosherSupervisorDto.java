package com.kosher.iskosher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

public record KosherSupervisorDto(
        UUID id,
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @NotBlank(message = "Contact info is required")
        @Size(max = 255, message = "Contact info must not exceed 255 characters")
        @JsonProperty("contact_info")
        String contactInfo,
        @NotBlank(message = "Authority is required") @Size(max = 100, message = "Authority " +
                "must not exceed 100 characters") String authority) implements Serializable {
}