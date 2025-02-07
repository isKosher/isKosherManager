package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.CertificateBusiness;
import com.kosher.iskosher.entity.KosherCertificate;
import com.kosher.iskosher.exception.BusinessCreationException;
import com.kosher.iskosher.exception.EntityNotFoundException;
import com.kosher.iskosher.exception.ResourceNotFoundException;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.repository.CertificateBusinessRepository;
import com.kosher.iskosher.repository.KosherCertificateRepository;
import com.kosher.iskosher.service.KosherCertificateService;
import com.kosher.iskosher.types.mappers.KosherCertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KosherCertificateServiceImpl implements KosherCertificateService {

    private final KosherCertificateRepository kosherCertificateRepository;
    private final BusinessRepository businessRepository;
    private final CertificateBusinessRepository certificateBusinessRepository;

    public List<KosherCertificateDto> getCertificates(UUID businessId) {
        return kosherCertificateRepository.findByCertificateVsBusinessesBusinessId(businessId)
                .stream()
                .map(KosherCertificateMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public KosherCertificate createCertificate(KosherCertificateDto dto) {
        try {
            return kosherCertificateRepository.save(KosherCertificateMapper.INSTANCE.toEntity(dto));
        } catch (Exception e) {
            throw new BusinessCreationException("An unexpected error occurred while creating the Kosher Certificate."
                    , e);
        }
    }

    @Override
    @Transactional
    public KosherCertificateDto createAndLinkCertificate(UUID businessId, KosherCertificateDto dto) {
        existsByCertificate(dto.certificate());

        KosherCertificate certificate = createCertificate(dto);
        certificate = kosherCertificateRepository.save(certificate);
        linkCertificateToBusiness(businessId, certificate);

        return KosherCertificateMapper.INSTANCE.toDTO(certificate);
    }

    @Override
    public void existsByCertificate(String certificate) {
        if (kosherCertificateRepository.existsByCertificate(certificate)) {
            throw new BusinessCreationException("Kosher Certificate with the given key already exists: " + certificate);
        }
    }

    @Override
    @Transactional
    public KosherCertificateDto updateCertificate(UUID businessId, KosherCertificateDto dto) {

        KosherCertificate certificate = kosherCertificateRepository.findByCertificate(dto.certificate())
                .orElseGet(() -> KosherCertificateMapper.INSTANCE.toEntity(dto));

        certificate.setExpirationDate(dto.expirationDate());
        kosherCertificateRepository.save(certificate);
        linkCertificateToBusiness(businessId, certificate);

        return KosherCertificateMapper.INSTANCE.toDTO(certificate);

    }

    @Override
    @Transactional
    public void deleteCertificate(UUID businessId, UUID certificateId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", businessId));

        KosherCertificate certificate = kosherCertificateRepository.findById(certificateId)
                .orElseThrow(() -> new EntityNotFoundException("KosherCertificate", "id", certificateId));

        CertificateBusiness certificateBusiness = certificateBusinessRepository
                .findByBusinessAndCertificate(business, certificate)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No link found between supervisor and business"));

        certificateBusinessRepository.delete(certificateBusiness);

        // if the certificate has no more business associations, we could delete them
        /*
        if (!certificatesBusinessRepository.existsByCertificateId(certificateId)) {
            kosherCertificateRepository.delete(certificate);
        }*/

    }

    private void linkCertificateToBusiness(UUID businessId, KosherCertificate certificate) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", businessId));

        CertificateBusiness existingLink = certificateBusinessRepository
                .findByBusinessAndCertificate(business, certificate)
                .orElse(null);

        if (existingLink == null) {
            CertificateBusiness certificateBusiness = new CertificateBusiness(business, certificate);
            certificateBusinessRepository.save(certificateBusiness);
        }
    }
}

