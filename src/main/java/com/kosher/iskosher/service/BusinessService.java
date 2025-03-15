package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.request.BusinessUpdateRequest;
import com.kosher.iskosher.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BusinessService {

    BusinessDetailedResponse getBusinessDetails(UUID id);

    void deleteBusiness(UUID id);

    BusinessResponse createBusiness(UUID userId, BusinessCreateRequest createRequest);

    Page<BusinessPreviewResponse> filterBusinesses(BusinessFilterCriteria criteria, Pageable pageable);

    List<BusinessSearchResponse> searchBusinesses(String searchTerm);

    PageResponse<BusinessPreviewResponse> getBusinessPreviews(Pageable pageable);

    BusinessResponse updateBusiness(UUID userId, BusinessUpdateRequest dto);

    Page<BusinessPreviewTravelResponse> getNearbyBusinesses(double latitude, double longitude, double radiusKm, Pageable pageable);
}
