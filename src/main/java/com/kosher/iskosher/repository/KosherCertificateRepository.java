package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.KosherCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KosherCertificateRepository extends JpaRepository<KosherCertificate, UUID> {
    List<KosherCertificate> findByCertificateVsBusinessesBusinessId(UUID businessId);
     boolean existsByCertificate(String certificate);

    Optional<KosherCertificate> findByCertificate(String certificate);

}