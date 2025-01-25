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

    @Query(nativeQuery = true)
    Optional<BusinessDetailedResponse> getBusinessDetails(@Param("businessId") UUID businessId);

    default List<BusinessSearchResponse> searchBusinesses(String searchTerm) {
        return searchBusinessesRaw(searchTerm)
                .stream()
                .map(BusinessSearchResponse::new)
                .collect(Collectors.toList());
    }

}