package com.kosher.iskosher.types;

import lombok.Builder;

@Builder
public record FoodCategoryTypeModel(
        boolean dairy,
        boolean meat,
        boolean pareve) {
}
