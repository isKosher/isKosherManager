package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessSearchCriteria;
import com.kosher.iskosher.dto.response.BusinessCreateResponse;
import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.dto.response.BusinessQuickSearchResponse;
import com.kosher.iskosher.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BusinessService {


    List<BusinessPreviewResponse> getBusinessPreviews();

    BusinessDetailedResponse getBusinessDetails(UUID id);

    void deleteBusiness(UUID id);

    BusinessCreateResponse createBusiness(BusinessCreateRequest createRequest);

    Page<BusinessPreviewResponse> searchBusinesses(BusinessSearchCriteria criteria, Pageable pageable);

    Page<BusinessQuickSearchResponse> quickSearchByName(String query, int limit);
}
