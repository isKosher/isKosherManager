package com.kosher.iskosher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record KosherCertificateDto(String certificate, @JsonProperty("expiration_date") LocalDate expirationDate) {}

