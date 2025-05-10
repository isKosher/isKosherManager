package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.LocationDto;
import com.kosher.iskosher.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "address", source = "address.name")
    @Mapping(target = "locationDetails", source = "details")
    @Mapping(target = "region", source = "city.region.name")
    LocationDto toDTO(Location location);


    @Mapping(target = "city.name", source = "city")
    @Mapping(target = "address.name", source = "address")
    @Mapping(target = "details", source = "locationDetails")
    Location toEntity(LocationDto locationDTO);

}