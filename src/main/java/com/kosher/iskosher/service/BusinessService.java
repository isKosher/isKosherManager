package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BusinessService {


    Page<BusinessPreviewResponse> getBusinessPreviews(Pageable pageable);

    BusinessDetailedResponse getBusinessDetails(UUID id);

    void deleteBusiness(UUID id);

    BusinessCreateResponse createBusiness(BusinessCreateRequest createRequest);

    Page<BusinessPreviewResponse> filterBusinesses(BusinessFilterCriteria criteria, Pageable pageable);

    List<BusinessSearchResponse> searchBusinesses(String searchTerm);

}
