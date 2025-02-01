package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.KosherCertificate;
import com.kosher.iskosher.exception.BusinessCreationException;
import com.kosher.iskosher.exception.ResourceNotFoundException;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.repository.KosherCertificateRepository;
import com.kosher.iskosher.service.KosherCertificateService;
import com.kosher.iskosher.types.mappers.KosherCertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KosherCertificateServiceImpl implements KosherCertificateService {

    private final KosherCertificateRepository kosherCertificateRepository;
    private final BusinessRepository businessRepository;

    //TODO replace all ResourceNotFoundException to EntityNotFoundException
    public KosherCertificateDto getKosherCertificate(UUID businessId) {
        KosherCertificate certificate = kosherCertificateRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + businessId));

        if (certificate == null) {
            throw new ResourceNotFoundException("No KosherCertificate associated with Business id: " + businessId);
        }
        return KosherCertificateMapper.INSTANCE.toDTO(certificate);
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

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + businessId));

        deleteExistingCertificateIfPresent(business);

        KosherCertificate certificate = createCertificate(dto);
        business.setKosherCertificate(certificate);
        businessRepository.save(business);

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
        existsByCertificate(dto.certificate());

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + businessId));

        KosherCertificate certificate = kosherCertificateRepository.findByCertificate(dto.certificate())
                .orElseGet(() -> {
                    deleteExistingCertificateIfPresent(business);
                    return createCertificate(dto);
                });

        certificate.setExpirationDate(dto.expirationDate());
        business.setKosherCertificate(certificate);
        businessRepository.save(business);

        return KosherCertificateMapper.INSTANCE.toDTO(kosherCertificateRepository.save(certificate));

    }

    //TODO: improve to other table like supervisors_businesses
    @Override
    @Transactional
    public void deleteCertificate(UUID businessId, UUID certificateId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + businessId));

        KosherCertificate certificate = kosherCertificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("KosherCertificate not found with id: " + certificateId));

        if (!business.getKosherCertificate().getId().equals(certificateId)) {
            throw new ResourceNotFoundException("The specified certificate is not associated with this business");
        }

        business.setKosherCertificate(null);
        businessRepository.save(business);
        kosherCertificateRepository.delete(certificate);
    }

    private void deleteExistingCertificateIfPresent(Business business) {
        Optional.ofNullable(business.getKosherCertificate())
                .ifPresent(kosherCertificateRepository::delete);
    }
}

