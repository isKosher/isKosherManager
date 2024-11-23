package com.kosher.iskosher.service.lookups;

import com.kosher.iskosher.common.lookup.AbstractLookupService;
import com.kosher.iskosher.dto.FoodTypeDto;
import com.kosher.iskosher.entity.FoodType;
import com.kosher.iskosher.repository.lookups.FoodTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodTypeService extends AbstractLookupService<FoodType, FoodTypeDto> {
    public FoodTypeService(FoodTypeRepository repository) {
        super(repository, FoodType.class);
    }

    @Override
    protected FoodType createEntity(String name) {
        FoodType newFoodType = new FoodType();
        newFoodType.setName(name);
        return newFoodType;
    }

    @Override
    protected FoodTypeDto mapToDto(FoodType entity) {
        return new FoodTypeDto(entity.getId(), entity.getName());
    }
}
