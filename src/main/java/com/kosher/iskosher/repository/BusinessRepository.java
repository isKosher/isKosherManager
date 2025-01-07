package com.kosher.iskosher.repository;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.dto.response.BusinessQuickSearchResponse;
import com.kosher.iskosher.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID>, CustomBusinessRepository {
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


    @Query("""
                SELECT DISTINCT new com.kosher.iskosher.dto.response.BusinessQuickSearchResponse(
                    b.id, 
                    b.name
                )
                FROM Business b
                WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                AND b.isActive = true
                ORDER BY b.name ASC
            """)
    Page<BusinessQuickSearchResponse> findByNameContainingIgnoreCase(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );


    @Query(nativeQuery = true)
    List<BusinessPreviewResponse> getAllBusinesses();


    @Query(nativeQuery = true)
    Optional<BusinessDetailedResponse> getBusinessDetails(@Param("businessId") UUID businessId);

}