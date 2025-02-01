package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.entity.KosherCertificate;

import java.util.UUID;

public interface KosherCertificateService {

    KosherCertificateDto getKosherCertificate(UUID businessId);

    KosherCertificate createCertificate(KosherCertificateDto kosherCertificateDto);

    KosherCertificateDto createAndLinkCertificate(UUID businessId, KosherCertificateDto dto);

    void existsByCertificate(String certificate);

    KosherCertificateDto updateCertificate(UUID businessId, KosherCertificateDto dto);

    void deleteCertificate(UUID businessId, UUID certificateId);
}
