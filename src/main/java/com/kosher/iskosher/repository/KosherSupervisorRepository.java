package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.KosherSupervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KosherSupervisorRepository extends JpaRepository<KosherSupervisor, UUID> {
}
