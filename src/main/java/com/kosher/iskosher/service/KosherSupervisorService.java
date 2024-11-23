package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.entity.KosherSupervisor;

public interface KosherSupervisorService {


    KosherSupervisor createSupervisor(KosherSupervisorDto kosherSupervisorDto);
}
