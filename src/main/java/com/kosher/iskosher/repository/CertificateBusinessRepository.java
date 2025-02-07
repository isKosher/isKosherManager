package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.CertificateBusiness;
import com.kosher.iskosher.entity.KosherCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CertificateBusinessRepository extends JpaRepository<CertificateBusiness, UUID> {

    Optional<CertificateBusiness> findByBusinessAndCertificate(Business business, KosherCertificate certificate);
}