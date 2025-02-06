package com.kosher.iskosher.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KosherTypeBusinessRepository extends JpaRepository<KosherTypeBusiness, UUID> {
}