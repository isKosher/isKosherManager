package com.kosher.iskosher.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class BusinessCreateResponse {
    private UUID businessId;
    private String businessName;
    private String businessNumber;
    private Instant createdAt;
}