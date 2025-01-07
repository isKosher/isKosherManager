package com.kosher.iskosher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BusinessQuickSearchResponse {
    private UUID id;
    private String name;
}