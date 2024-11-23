package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.KosherCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KosherCertificateRepository extends JpaRepository<KosherCertificate, UUID> {
}