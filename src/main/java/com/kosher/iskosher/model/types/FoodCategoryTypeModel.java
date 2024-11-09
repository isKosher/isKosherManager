package com.kosher.iskosher.model.types;

import lombok.Builder;

@Builder
public record FoodCategoryTypeModel(
        boolean dairy,
        boolean meat,
        boolean pareve) {
}
