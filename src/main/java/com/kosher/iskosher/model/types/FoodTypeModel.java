package com.kosher.iskosher.model.types;

import lombok.Builder;
import lombok.Data;

@Data
public class FoodTypeModel {
   private boolean dairy;
    private boolean meat;
    private boolean pareve;

}
