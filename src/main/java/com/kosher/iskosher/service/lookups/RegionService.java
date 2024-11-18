package com.kosher.iskosher.service.lookups;


import com.kosher.iskosher.common.lookup.AbstractLookupService;
import com.kosher.iskosher.dto.RegionDto;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.entity.Region;
import com.kosher.iskosher.repository.lookups.RegionRepository;
import org.springframework.stereotype.Service;

@Service
public class RegionService extends AbstractLookupService<Region, RegionDto> {
    public RegionService(RegionRepository repository) {
        super(repository, Region.class);
    }

    @Override
    protected Region createEntity(RegionDto dto) {
        Region newRegion = new Region();
        newRegion.setName(dto.name());
        return newRegion;
    }

    @Override
    protected RegionDto mapToDto(Region entity) {
        return new RegionDto(entity.getId(), entity.getName());
    }
}
