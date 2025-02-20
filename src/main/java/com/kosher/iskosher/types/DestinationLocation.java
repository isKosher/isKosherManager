package com.kosher.iskosher.types;

import lombok.Builder;

@Builder
public record DestinationLocation(Double latitude, Double longitude) {
}
