package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.KosherSupervisor;
import com.kosher.iskosher.entity.SupervisorsBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SupervisorsBusinessRepository extends JpaRepository<SupervisorsBusiness, UUID> {
    boolean existsByBusinessAndSupervisor(Business business, KosherSupervisor supervisor);
    Optional<SupervisorsBusiness> findByBusinessAndSupervisor(Business business, KosherSupervisor supervisor);
}