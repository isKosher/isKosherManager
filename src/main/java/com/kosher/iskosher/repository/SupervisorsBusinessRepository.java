package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.SupervisorsBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupervisorsBusinessRepository extends JpaRepository<SupervisorsBusiness, UUID> {
}