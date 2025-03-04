package com.kosher.iskosher.repository;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewTravelResponse;
import com.kosher.iskosher.dto.response.BusinessSearchResponse;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.service.TravelTimeService;
import com.kosher.iskosher.types.DestinationLocation;
import com.kosher.iskosher.types.TravelInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
import java.util.stream.Collectors;

public interface BusinessRepository extends JpaRepository<Business, UUID>, CustomBusinessRepository {

    double EARTH_RADIUS_KM = 6371.0;

    //TODO: add Pageable to this actions
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

    default List<BusinessSearchResponse> searchBusinesses(String searchTerm) {
        return searchBusinessesRaw(searchTerm)
                .stream()
                .map(BusinessSearchResponse::new)
                .collect(Collectors.toList());
    }

    @Query(value = "SELECT * FROM get_nearby_businesses_with_travel(:lat, :lon, :radius, :earthRadius, :limitParam, :offsetParam)",
            nativeQuery = true)
    List<Object[]> getNearbyBusinessesRaw(
            @Param("lat") double centerLat,
            @Param("lon") double centerLon,
            @Param("radius") double radiusKm,
            @Param("earthRadius") double earthRadiusKm,
            @Param("limitParam") Integer limitParam,
            @Param("offsetParam") Integer offsetParam
    );
    default Page<BusinessPreviewTravelResponse> getNearbyBusinesses(
            double centerLat,
            double centerLon,
            double radiusKm,
            TravelTimeService osrmService,
            Pageable pageable) {

        int expandedSize = pageable.getPageSize() * 2;

        List<Object[]> rawResults = getNearbyBusinessesRaw(
                centerLat, centerLon, radiusKm, EARTH_RADIUS_KM,
                expandedSize, (int) pageable.getOffset()
        );

        List<BusinessPreviewTravelResponse> businesses = rawResults.stream()
                .map(row -> {
                    TravelInfo travelInfo = osrmService.getTravelInfo(
                            centerLat, centerLon,
                            (Double) row[6], (Double) row[7]
                    );

                    return new BusinessPreviewTravelResponse(
                            (UUID) row[0],  // id
                            (String) row[1], // name
                            (String) row[8], // address
                            (String) row[9], // street_number
                            (String) row[2], // city
                            (Integer) row[3], // business_type
                            (String) row[4], // food_types
                            (String) row[10], // food_item_types
                            (String) row[11], // kosher_types
                            (String) row[5], // business_photos
                            travelInfo, 
                            new DestinationLocation((Double) row[6], (Double) row[7])
                    );
                })
                .filter(dto -> {
                    String distanceStr = dto.getTravelInfo().drivingDistance()
                            .replace(" ק\"מ", "");
                    double actualDistance = Double.parseDouble(distanceStr);
                    return actualDistance <= radiusKm;
                })
                .sorted(Comparator.comparingDouble(dto -> {
                    String distanceStr = dto.getTravelInfo().drivingDistance()
                            .replace(" ק\"מ", "");
                    return Double.parseDouble(distanceStr);
                }))
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        long totalElements = rawResults.size() + pageable.getOffset();

        return new PageImpl<>(businesses, pageable, totalElements);
    }
}