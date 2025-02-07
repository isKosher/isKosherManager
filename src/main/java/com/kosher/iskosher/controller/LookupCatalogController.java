package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.BusinessTypeDto;
import com.kosher.iskosher.dto.FoodItemTypeDto;
import com.kosher.iskosher.dto.FoodTypeDto;
import com.kosher.iskosher.dto.KosherTypeDto;
import com.kosher.iskosher.service.lookups.BusinessTypeService;
import com.kosher.iskosher.service.lookups.FoodItemTypeService;
import com.kosher.iskosher.service.lookups.FoodTypeService;
import com.kosher.iskosher.service.lookups.KosherTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lookup")
@RequiredArgsConstructor
public class LookupCatalogController {
    private final FoodTypeService foodTypeService;
    private final FoodItemTypeService foodItemTypeService;
    private final KosherTypeService kosherTypeService;
    private final BusinessTypeService businessTypeService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLookupData() {
        Map<String, Object> allLookupData = new HashMap<>();

        allLookupData.put("food_types", foodTypeService.findAll());
        allLookupData.put("food_item_types", foodItemTypeService.findAll());
        allLookupData.put("kosher_types", kosherTypeService.findAll());
        allLookupData.put("business_types", businessTypeService.findAll());

        return ResponseEntity.ok(allLookupData);
    }

    @GetMapping("/food-types")
    public ResponseEntity<List<FoodTypeDto>> getFoodTypes() {
        return ResponseEntity.ok(foodTypeService.findAll());
    }

    @GetMapping("/food-item-types")
    public ResponseEntity<List<FoodItemTypeDto>> getFoodItemTypes() {
        return ResponseEntity.ok(foodItemTypeService.findAll());
    }

    @GetMapping("/kosher-types")
    public ResponseEntity<List<KosherTypeDto>> getKosherTypes() {
        return ResponseEntity.ok(kosherTypeService.findAll());
    }

    @GetMapping("/business-types")
    public ResponseEntity<List<BusinessTypeDto>> getBusinessTypes() {
        return ResponseEntity.ok(businessTypeService.findAll());
    }
}
