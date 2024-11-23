package com.kosher.iskosher.service.lookups;

import com.kosher.iskosher.common.lookup.AbstractLookupService;
import com.kosher.iskosher.dto.CityDto;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.repository.lookups.CityRepository;
import com.kosher.iskosher.repository.lookups.RegionRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService extends AbstractLookupService<City, CityDto> {
    private final RegionService regionService;
    public CityService(CityRepository repository, RegionService regionService) {
        super(repository, City.class);
        this.regionService = regionService;
    }

    @Override
    protected City createEntity(String name) {
        City newCity = new City();
        newCity.setName(name);
        return newCity;
    }

    @Override
    protected CityDto mapToDto(City entity) {
        return new CityDto(entity.getId(), entity.getName());
    }

    public City createCity(String cityName, String regionName) {
        City newCity = new City();
        newCity.setName(cityName);
        newCity.setRegion(regionService.getOrCreateEntity(regionName));
        return newCity;
    }
}