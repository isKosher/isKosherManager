package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.entity.KosherSupervisor;

import java.util.List;
import java.util.UUID;

public interface KosherSupervisorService {

    KosherSupervisorDto createAndLinkSupervisor(UUID businessId, KosherSupervisorDto dto);
    KosherSupervisor createSupervisorOnly(KosherSupervisorDto dto);
    List<KosherSupervisorDto> getSupervisors(UUID businessId);
    KosherSupervisorDto updateSupervisor(UUID businessId, KosherSupervisorDto dto);
    void deleteSupervisorFromBusiness(UUID businessId, UUID supervisorId);
}
