package com.kosher.iskosher.types;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record KosherCertificateModel(String certificate, LocalDate expirationDate) {
}
