package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.entity.KosherCertificate;

public interface KosherCertificateService {

    KosherCertificate createCertificate(KosherCertificateDto kosherCertificateDto);
}
