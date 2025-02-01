package com.kosher.iskosher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public record KosherCertificateDto(
        UUID id,
        @NotBlank(message = "Certificate is required")
        String certificate,
        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration date must be in the future")
        @JsonProperty("expiration_date")
        LocalDate expirationDate) implements Serializable {
}

