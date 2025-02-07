package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.KosherSupervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KosherSupervisorRepository extends JpaRepository<KosherSupervisor, UUID> {

    List<KosherSupervisor> findBySupervisorsVsBusinessesBusinessId(UUID businessId);
    Optional<KosherSupervisor> findByContactInfo(String contactInfo);
}
