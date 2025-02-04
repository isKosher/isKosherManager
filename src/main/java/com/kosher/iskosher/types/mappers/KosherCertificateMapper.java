package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.entity.KosherCertificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KosherCertificateMapper {
    KosherCertificateMapper INSTANCE = Mappers.getMapper(KosherCertificateMapper.class);

    KosherCertificateDto toDTO(KosherCertificate entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "certificateBusinesses", ignore = true)
    KosherCertificate toEntity(KosherCertificateDto dto);
}