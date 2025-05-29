package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.entity.BusinessPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BusinessPhotoMapper {
    BusinessPhotoMapper INSTANCE = Mappers.getMapper(BusinessPhotoMapper.class);

    BusinessPhotoDto toDTO(BusinessPhoto entity);

    @Mapping(target = "businessPhotosVsBusinesses", ignore = true)
    BusinessPhoto toEntity(BusinessPhotoDto dto);
}
