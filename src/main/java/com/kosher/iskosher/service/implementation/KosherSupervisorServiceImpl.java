package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.entity.KosherSupervisor;
import com.kosher.iskosher.repository.KosherSupervisorRepository;
import com.kosher.iskosher.service.KosherSupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KosherSupervisorServiceImpl implements KosherSupervisorService {

    private final KosherSupervisorRepository kosherSupervisorRepository;
    @Override
    public KosherSupervisor createSupervisor(KosherSupervisorDto kosherSupervisorDto) {
            KosherSupervisor supervisor = new KosherSupervisor();
            supervisor.setName(kosherSupervisorDto.name());
            supervisor.setContactInfo(kosherSupervisorDto.contactInfo());
            supervisor.setAuthority(kosherSupervisorDto.contactInfo());
            return kosherSupervisorRepository.save(supervisor);

    }
}
