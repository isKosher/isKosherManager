package com.kosher.iskosher.repository;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewTravelResponse;
import com.kosher.iskosher.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID>, CustomBusinessRepository {

    //TODO: add Pageable to this actions and check about Spring Data JPA Projections
    @Query(value = "SELECT * FROM search_businesses(:searchTerm)", nativeQuery = true)
    List<Object[]> searchBusinessesRaw(@Param("searchTerm") String searchTerm);

    @Query(nativeQuery = true)
    List<BusinessPreviewResponse> getAllBusinesses(@Param("limitParam") Integer limitParam,
                                                   @Param("offsetParam") Integer offsetParam);

    long countByIsActiveTrue();

    @Query("""
            SELECT DISTINCT b FROM Business b
            LEFT JOIN FETCH b.kosherTypeVsBusinesses ktb
            LEFT JOIN FETCH ktb.kosherType kt
            LEFT JOIN FETCH b.businessType bt
            LEFT JOIN FETCH b.location l
            LEFT JOIN FETCH l.address a
            LEFT JOIN FETCH l.city c
            LEFT JOIN FETCH b.certificateVsBusinesses cb
            LEFT JOIN FETCH cb.certificate kc
            LEFT JOIN FETCH b.supervisorsVsBusinesses sb
            LEFT JOIN FETCH sb.supervisor ks
            LEFT JOIN FETCH b.foodTypeVsBusinesses ftb
            LEFT JOIN FETCH ftb.foodType ft
            LEFT JOIN FETCH b.businessPhotosVsBusinesses bpb
            LEFT JOIN FETCH bpb.businessPhotos bp
            LEFT JOIN FETCH b.foodItemTypeVsBusinesses fitb
            LEFT JOIN FETCH fitb.foodItemType fit
            WHERE EXISTS (
                SELECT 1
                FROM UsersBusiness ub
                WHERE ub.business = b
                AND ub.user.id = :userId
            )
            AND b.isActive = true
            """)
    List<Business> findAllBusinessDetailsByUserId(@Param("userId") UUID userId);

    @Query(nativeQuery = true)
    Optional<BusinessDetailedResponse> getBusinessDetails(@Param("businessId") UUID businessId);

    @Query(nativeQuery = true)
    List<BusinessPreviewTravelResponse> getNearbyBusinesses(
            @Param("lat") double centerLat,
            @Param("lon") double centerLon,
            @Param("radius") double radiusKm,
            @Param("earthRadius") double earthRadiusKm,
            @Param("limitParam") Integer limitParam,
            @Param("offsetParam") Integer offsetParam
    );
}