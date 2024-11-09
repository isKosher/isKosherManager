package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {
    @Query("SELECT DISTINCT b FROM Business b " +
            "LEFT JOIN FETCH b.kosherType " +
            "LEFT JOIN FETCH b.kosherCertificate " +
            "LEFT JOIN FETCH b.businessType " +
            "LEFT JOIN FETCH b.foodTypeVsBusinesses ftb " +
            "LEFT JOIN FETCH b.locationsVsBusinesses lb " +
            "LEFT JOIN FETCH b.supervisorsVsBusinesses sb " +
            "WHERE b.isActive = true")
    List<Business> findAllBusinessWithDetails();

    @Query("""
        SELECT DISTINCT b FROM Business b
        LEFT JOIN FETCH b.businessType
        LEFT JOIN FETCH b.kosherType
        LEFT JOIN FETCH b.locationsVsBusinesses lb
        LEFT JOIN FETCH lb.location l
        LEFT JOIN FETCH l.city c
        LEFT JOIN FETCH c.region r
        LEFT JOIN FETCH l.address a
        LEFT JOIN FETCH b.foodTypeVsBusinesses fb
        LEFT JOIN FETCH b.supervisorsVsBusinesses sb
        LEFT JOIN FETCH fb.foodType
        WHERE b.isActive = true
    """)
    List<Business> findAllActiveBusinessesWithDetails();

}