package com.kosher.iskosher.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record KosherCertificateModel(String certificate, LocalDate expirationDate) {
}
