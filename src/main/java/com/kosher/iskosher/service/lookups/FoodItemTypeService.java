package com.kosher.iskosher.service.lookups;

import com.kosher.iskosher.common.lookup.AbstractLookupService;
import com.kosher.iskosher.common.lookup.LookupRepository;
import com.kosher.iskosher.dto.FoodItemTypeDto;
import com.kosher.iskosher.entity.FoodItemType;
import com.kosher.iskosher.repository.lookups.FoodItemTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodItemTypeService extends AbstractLookupService<FoodItemType, FoodItemTypeDto> {
    public FoodItemTypeService(FoodItemTypeRepository repository) {
        super(repository, FoodItemType.class);

    }
    @Override
    protected FoodItemType createEntity(String name) {
        FoodItemType newFoodItemType = new FoodItemType();
        newFoodItemType.setName(name);
        return newFoodItemType;
    }

    @Override
    protected FoodItemTypeDto mapToDto(FoodItemType entity) {
        return new FoodItemTypeDto(entity.getId(), entity.getName());
    }
}