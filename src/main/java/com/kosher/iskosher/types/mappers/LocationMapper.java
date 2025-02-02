package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.LocationDto;
import com.kosher.iskosher.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    KosherCertificateMapper INSTANCE = Mappers.getMapper(KosherCertificateMapper.class);

    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "address", source = "address.name")
    LocationDto toDTO(Location location);

    @Mapping(target = "city.name", source = "city")
    @Mapping(target = "address.name", source = "address")
    Location toEntity(LocationDto locationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city.name", source = "city")
    @Mapping(target = "address.name", source = "address")
    void updateEntityFromDTO(LocationDto locationDTO, @MappingTarget Location location);
}