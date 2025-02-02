package com.kosher.iskosher.repository;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.dto.response.BusinessSearchResponse;
import com.kosher.iskosher.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface BusinessRepository extends JpaRepository<Business, UUID>, CustomBusinessRepository {

    //TODO: add Pageable to this actions
    @Query(value = "SELECT * FROM search_businesses(:searchTerm)", nativeQuery = true)
    List<Object[]> searchBusinessesRaw(@Param("searchTerm") String searchTerm);

    @Query(nativeQuery = true)
    List<BusinessPreviewResponse> getAllBusinesses(@Param("limitParam") Integer limitParam,
                                                   @Param("offsetParam") Integer offsetParam);

    long countByIsActiveTrue();

    @Query("""
            SELECT DISTINCT b FROM Business b
            LEFT JOIN FETCH b.kosherType kt
            LEFT JOIN FETCH b.kosherCertificate kc
            LEFT JOIN FETCH b.businessType bt
            LEFT JOIN FETCH b.locationsVsBusinesses lb
            LEFT JOIN FETCH lb.location l
            LEFT JOIN FETCH l.address a
            LEFT JOIN FETCH l.city c
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

    default List<BusinessSearchResponse> searchBusinesses(String searchTerm) {
        return searchBusinessesRaw(searchTerm)
                .stream()
                .map(BusinessSearchResponse::new)
                .collect(Collectors.toList());
    }

    @Query("""
        SELECT COUNT(ub) > 0 
        FROM UsersBusiness ub 
        WHERE ub.business.id = :businessId 
        AND ub.user.id = :userId
    """)
    boolean isBusinessManagedByUser(@Param("businessId") UUID businessId, @Param("userId") UUID userId);

}