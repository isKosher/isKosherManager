package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.response.*;
import com.kosher.iskosher.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BusinessService {

    BusinessDetailedResponse getBusinessDetails(UUID id);

    List<BusinessDetailedResponses> getBusinessDetailsByUserId(UUID userId);

    void deleteBusiness(UUID id);

    BusinessCreateResponse createBusiness(UUID userId, BusinessCreateRequest createRequest);

    Page<BusinessPreviewResponse> filterBusinesses(BusinessFilterCriteria criteria, Pageable pageable);

    List<BusinessSearchResponse> searchBusinesses(String searchTerm);

    PageResponse<BusinessPreviewResponse> getBusinessPreviews(Pageable pageable);
}
