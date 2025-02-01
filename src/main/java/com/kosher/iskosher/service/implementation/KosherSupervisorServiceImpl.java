package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.KosherSupervisor;
import com.kosher.iskosher.entity.SupervisorsBusiness;
import com.kosher.iskosher.exception.ResourceNotFoundException;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.repository.KosherSupervisorRepository;
import com.kosher.iskosher.repository.SupervisorsBusinessRepository;
import com.kosher.iskosher.service.KosherSupervisorService;
import com.kosher.iskosher.types.mappers.KosherSupervisorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KosherSupervisorServiceImpl implements KosherSupervisorService {

    private final KosherSupervisorRepository kosherSupervisorRepository;

    private final BusinessRepository businessRepository;

    private final SupervisorsBusinessRepository supervisorsBusinessRepository;

    @Override
    public KosherSupervisor createSupervisorOnly(KosherSupervisorDto dto) {
        Optional<KosherSupervisor> existingSupervisor =
                kosherSupervisorRepository.findByContactInfo(dto.contactInfo());
        if (existingSupervisor.isPresent()) {
            return existingSupervisor.get();
        }
        KosherSupervisor supervisor = KosherSupervisorMapper.INSTANCE.toEntity(dto);

        return kosherSupervisorRepository.save(supervisor);
    }

    @Override
    @Transactional
    public KosherSupervisorDto createAndLinkSupervisor(UUID businessId, KosherSupervisorDto dto) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        KosherSupervisor supervisor = kosherSupervisorRepository.findByContactInfo(dto.contactInfo())
                .orElseGet(() -> kosherSupervisorRepository.save(KosherSupervisorMapper.INSTANCE.toEntity(dto)));

        boolean alreadyLinked = supervisorsBusinessRepository.existsByBusinessAndSupervisor(business, supervisor);
        if (alreadyLinked) {
            throw new ResourceNotFoundException("Supervisor is already linked to this business");
        }

        SupervisorsBusiness supervisorsBusiness = new SupervisorsBusiness(business, supervisor);
        supervisorsBusinessRepository.save(supervisorsBusiness);

        return KosherSupervisorMapper.INSTANCE.toDTO(supervisor);
    }

    public List<KosherSupervisorDto> getSupervisors(UUID businessId) {
        return kosherSupervisorRepository.findBySupervisorsVsBusinessesBusinessId(businessId)
                .stream()
                .map(KosherSupervisorMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public KosherSupervisorDto updateSupervisor(UUID businessId, KosherSupervisorDto dto) {
        Optional<KosherSupervisor> existingSupervisor = kosherSupervisorRepository.findByContactInfo(dto.contactInfo());
        if (existingSupervisor.isPresent()) {
            KosherSupervisor supervisor = existingSupervisor.get();
            supervisor.setName(dto.name());
            supervisor.setAuthority(dto.authority());
            return KosherSupervisorMapper.INSTANCE.toDTO(kosherSupervisorRepository.save(supervisor));
        } else {
            return createAndLinkSupervisor(businessId, dto);
        }
    }

    @Override
    @Transactional
    public void deleteSupervisorFromBusiness(UUID businessId, UUID supervisorId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        KosherSupervisor supervisor = kosherSupervisorRepository.findById(supervisorId)
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor not found"));

        SupervisorsBusiness supervisorsBusiness = supervisorsBusinessRepository
                .findByBusinessAndSupervisor(business, supervisor)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No link found between supervisor and business"));

        supervisorsBusinessRepository.delete(supervisorsBusiness);

        // if the supervisor has no more business associations, we could delete them
        /*
        if (!supervisorsBusinessRepository.existsBySupervisorId(supervisorId)) {
            kosherSupervisorRepository.delete(supervisor);
        }*/
    }

}
