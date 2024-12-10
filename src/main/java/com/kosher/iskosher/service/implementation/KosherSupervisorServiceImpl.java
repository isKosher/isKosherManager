package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.entity.KosherSupervisor;
import com.kosher.iskosher.repository.KosherSupervisorRepository;
import com.kosher.iskosher.service.KosherSupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KosherSupervisorServiceImpl implements KosherSupervisorService {

    private final KosherSupervisorRepository kosherSupervisorRepository;
    @Override
    public KosherSupervisor createSupervisor(KosherSupervisorDto kosherSupervisorDto) {
        Optional<KosherSupervisor> existingSupervisor = kosherSupervisorRepository.findByContactInfo(kosherSupervisorDto.contactInfo());
        if (existingSupervisor.isPresent()) {
            return existingSupervisor.get();
        }
        KosherSupervisor supervisor = new KosherSupervisor();
        supervisor.setName(kosherSupervisorDto.name());
        supervisor.setContactInfo(kosherSupervisorDto.contactInfo());
        supervisor.setAuthority(kosherSupervisorDto.authority());
        return kosherSupervisorRepository.save(supervisor);
    }

}
