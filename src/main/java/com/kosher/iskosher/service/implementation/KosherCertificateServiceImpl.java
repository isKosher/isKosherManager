package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.entity.KosherCertificate;
import com.kosher.iskosher.exception.BusinessCreationException;
import com.kosher.iskosher.repository.KosherCertificateRepository;
import com.kosher.iskosher.service.KosherCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class KosherCertificateServiceImpl implements KosherCertificateService {

    private final KosherCertificateRepository kosherCertificateRepository;

    @Override
    public KosherCertificate createCertificate(KosherCertificateDto dto) {
        try {
            KosherCertificate certificate = new KosherCertificate();
            certificate.setCertificate(dto.certificate());
            certificate.setExpirationDate(dto.expirationDate());
            return kosherCertificateRepository.save(certificate);
        } catch (BusinessCreationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessCreationException("An unexpected error occurred while creating the Kosher Certificate."
                    , e);
        }
    }

    @Override
    public void existsByCertificate(String certificate) {
        if (kosherCertificateRepository.existsByCertificate(certificate)) {
            throw new BusinessCreationException("Kosher Certificate with the given key already exists: " + certificate);
        }
    }

}
