package com.kosher.iskosher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KosherSupervisorDto(String name, @JsonProperty("contact_info") String contactInfo, String authority) {
}

