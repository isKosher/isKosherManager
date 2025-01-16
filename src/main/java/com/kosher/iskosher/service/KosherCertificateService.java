package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.entity.KosherCertificate;
import com.kosher.iskosher.exception.BusinessCreationException;

public interface KosherCertificateService {

    KosherCertificate createCertificate(KosherCertificateDto kosherCertificateDto);

    void existsByCertificate(String certificate);


}
