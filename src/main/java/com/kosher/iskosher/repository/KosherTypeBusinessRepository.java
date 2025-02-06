package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.KosherTypeBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KosherTypeBusinessRepository extends JpaRepository<KosherTypeBusiness, UUID> {
}