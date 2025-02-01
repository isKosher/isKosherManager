package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.KosherCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface KosherCertificateRepository extends JpaRepository<KosherCertificate, UUID> {
    boolean existsByCertificate(String certificate);

    Optional<KosherCertificate> findByCertificate(String certificate);

    @Query("SELECT kc FROM KosherCertificate kc JOIN FETCH kc.businesses b WHERE b.id = :businessId")
    Optional<KosherCertificate> findByBusinessId(@Param("businessId") UUID businessId);
}