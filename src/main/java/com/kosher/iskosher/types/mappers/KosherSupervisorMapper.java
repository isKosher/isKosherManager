package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.entity.KosherSupervisor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KosherSupervisorMapper {
    KosherSupervisorMapper INSTANCE = Mappers.getMapper(KosherSupervisorMapper.class);

    KosherSupervisorDto toDTO(KosherSupervisor entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supervisorsVsBusinesses", ignore = true)
    KosherSupervisor toEntity(KosherSupervisorDto dto);

}
