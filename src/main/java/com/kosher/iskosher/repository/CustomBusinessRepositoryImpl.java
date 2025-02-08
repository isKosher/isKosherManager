package com.kosher.iskosher.repository;

import com.kosher.iskosher.common.enums.ErrorType;
import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.exception.BusinessFilterException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Slf4j
public class CustomBusinessRepositoryImpl implements CustomBusinessRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<BusinessPreviewResponse> filterBusinesses(BusinessFilterCriteria criteria, Pageable pageable) {
        try {
            validateSearchCriteria(criteria);
            validatePageable(pageable);
            String queryString = buildSearchQuery(criteria);
            Map<String, Object> parameters = new HashMap<>();

            Query query = entityManager.createNativeQuery(queryString);
            Query countQuery = entityManager.createNativeQuery(buildCountQuery(criteria));

            setQueryParameters(query, countQuery, criteria, parameters);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<Object[]> results = query.getResultList();
            long total = ((Number) countQuery.getSingleResult()).longValue();

            List<BusinessPreviewResponse> businessPreviews = processResults(results);
            return new PageImpl<>(businessPreviews, pageable, total);

        } catch (PersistenceException e) {
            log.error("Database error: {}", e.getMessage(), e);
            throw new BusinessFilterException("Database error occurred", e, ErrorType.DATABASE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("Invalid filter criteria: {}", e.getMessage(), e);
            throw new BusinessFilterException("Invalid filter criteria", e, ErrorType.INVALID_FILTER);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw new BusinessFilterException("Unexpected error while filtering businesses", e, ErrorType.UNKNOWN);
        }
    }


    private String buildSearchQuery(BusinessFilterCriteria criteria) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT " +
                        "b.id, " +
                        "b.name, " +
                        // Aggregate food types using the new junction table
                        "CAST(CONCAT('[\"', STRING_AGG(DISTINCT ft.name, '\",\"'), '\"]') AS text) as food_types, " +
                        // Aggregate food item types using the new junction table
                        "CAST(CONCAT('[\"', STRING_AGG(DISTINCT fit.name, '\",\"'), '\"]') AS text) as " +
                        "food_item_types, " +
                        "a.name, " +
                        "l.street_number, " +
                        "c.name, " +
                        // Updated photo aggregation with ID
                        "CAST(CONCAT('[', STRING_AGG(DISTINCT " +
                        "CONCAT('{\"id\":\"', bp.id, '\",\"url\":\"', bp.url, '\",\"photo_info\":\"', COALESCE(bp" +
                        ".photo_info, ''), '\"}'), '," +
                        "'), ']') AS text) as photos, " +
                        // Get kosher type with ID
                        "CAST(CONCAT('[', STRING_AGG(DISTINCT " +
                        "CONCAT('{\"id\":\"', kt.id, '\",\"name\":\"', kt.name, '\",\"kosher_icon_url\":\"', COALESCE" +
                        "(kt.kosher_icon_url, ''), '\"}'), '," +
                        "'), ']') AS text) as kosher_types, " +
                        "bt.name, " +
                        "b.rating " +
                        "FROM businesses b " +
                        "JOIN business_type bt ON b.business_type = bt.id " +
                        // Updated joins for food types
                        "LEFT JOIN food_type_businesses ftb ON b.id = ftb.business_id " +
                        "LEFT JOIN food_types ft ON ftb.food_type_id = ft.id " +
                        // Updated joins for food item types
                        "LEFT JOIN food_item_type_businesses fitb ON b.id = fitb.business_id " +
                        "LEFT JOIN food_item_type fit ON fitb.food_item_type_id = fit.id " +
                        // Updated location joins
                        "LEFT JOIN locations l ON b.location_id = l.id " +
                        "LEFT JOIN cities c ON l.city_id = c.id " +
                        "LEFT JOIN addresses a ON l.address_id = a.id " +
                        // Updated kosher type joins
                        "LEFT JOIN kosher_type_businesses ktb ON b.id = ktb.business_id " +
                        "LEFT JOIN kosher_types kt ON ktb.kosher_type_id = kt.id " +
                        // Updated photo joins
                        "LEFT JOIN business_photos_businesses bpb ON b.id = bpb.businesses_id " +
                        "LEFT JOIN business_photos bp ON bpb.business_photos_id = bp.id " +
                        "WHERE b.is_active = true ");

        appendCriteriaConditions(queryBuilder, criteria);

        queryBuilder.append("GROUP BY b.id, b.name, a.name, l.street_number, c.name, bt.name, b.rating ");
        queryBuilder.append("ORDER BY b.rating DESC NULLS LAST, b.name ASC");

        return queryBuilder.toString();
    }

    private String buildCountQuery(BusinessFilterCriteria criteria) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT COUNT(DISTINCT b.id) FROM businesses b " +
                        "JOIN business_type bt ON b.business_type = bt.id " +
                        "LEFT JOIN food_type_businesses ftb ON b.id = ftb.business_id " +
                        "LEFT JOIN food_types ft ON ftb.food_type_id = ft.id " +
                        "LEFT JOIN food_item_type_businesses fitb ON b.id = fitb.business_id " +
                        "LEFT JOIN food_item_type fit ON fitb.food_item_type_id = fit.id " +
                        "LEFT JOIN locations l ON b.location_id = l.id " +
                        "LEFT JOIN cities c ON l.city_id = c.id " +
                        "LEFT JOIN kosher_type_businesses ktb ON b.id = ktb.business_id " +
                        "LEFT JOIN kosher_types kt ON ktb.kosher_type_id = kt.id " +
                        "WHERE b.is_active = true ");

        appendCriteriaConditions(queryBuilder, criteria);

        return queryBuilder.toString();
    }

    private void appendCriteriaConditions(StringBuilder queryBuilder, BusinessFilterCriteria criteria) {
        if (criteria.getBusinessType() != null) {
            queryBuilder.append("AND bt.name = :businessType ");
        }
        if (criteria.getFoodType() != null) {
            queryBuilder.append("AND ft.name = :foodType ");
        }
        if (criteria.getFoodItemType() != null) {
            queryBuilder.append("AND fit.name = :foodItemType ");
        }
        if (criteria.getCity() != null) {
            queryBuilder.append("AND c.name = :city ");
        }
        if (criteria.getKosherType() != null) {
            queryBuilder.append("AND kt.name = :kosherType ");
        }
        if (criteria.getRating() != null) {
            queryBuilder.append("AND b.rating >= :rating ");
        }
    }

    private void setQueryParameters(Query query, Query countQuery, BusinessFilterCriteria criteria,
                                    Map<String, Object> parameters) {
        if (criteria.getBusinessType() != null) {
            parameters.put("businessType", criteria.getBusinessType());
        }
        if (criteria.getFoodType() != null) {
            parameters.put("foodType", criteria.getFoodType());
        }
        if (criteria.getFoodItemType() != null) {
            parameters.put("foodItemType", criteria.getFoodItemType());
        }
        if (criteria.getCity() != null) {
            parameters.put("city", criteria.getCity());
        }
        if (criteria.getKosherType() != null) {
            parameters.put("kosherType", criteria.getKosherType());
        }
        if (criteria.getRating() != null) {
            parameters.put("rating", criteria.getRating());
        }

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
    }

    private List<BusinessPreviewResponse> processResults(List<Object[]> results) {
        List<BusinessPreviewResponse> businessList = new ArrayList<>();

        for (Object[] row : results) {
            BusinessPreviewResponse business = new BusinessPreviewResponse(
                    (UUID) row[0],                // businessId
                    (String) row[1],              // businessName
                    (String) row[2],              // foodTypes -JSON string
                    (String) row[3],              // foodItemTypes -JSON string
                    (String) row[4],              // address
                    (Integer) row[5],             // streetNumber
                    (String) row[6],              // city
                    (String) row[7],              // businessPhotos -JSON string
                    (String) row[8],              // kosherType
                    (String) row[9]               // businessType
            );

            businessList.add(business);
        }

        return businessList;
    }

    private void validateSearchCriteria(BusinessFilterCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Search criteria cannot be null");
        }
    }

    private void validatePageable(Pageable pageable) {
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        if (pageable.getPageNumber() < 0) {
            throw new IllegalArgumentException("Page number must be non-negative");
        }
    }
}