package com.kosher.iskosher.service.lookups;

import com.kosher.iskosher.common.lookup.AbstractLookupService;
import com.kosher.iskosher.dto.CityDto;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.repository.lookups.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService extends AbstractLookupService<City, CityDto> {
    public CityService(CityRepository repository) {
        super(repository, City.class);
    }

    @Override
    protected City createEntity(CityDto city) {
        City newCity = new City();
        newCity.setName(city.name());
        return newCity;
    }

    @Override
    protected CityDto mapToDto(City entity) {
        return new CityDto(entity.getId(), entity.getName());
    }
}