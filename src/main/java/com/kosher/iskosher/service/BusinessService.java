package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.response.BusinessCreateResponse;
import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;

import java.util.List;
import java.util.UUID;

public interface BusinessService {


    List<BusinessPreviewResponse> getBusinessPreviews();

    BusinessDetailedResponse getBusinessDetails(UUID id);

    void deleteBusiness(UUID id);

    BusinessCreateResponse createBusiness(BusinessCreateRequest createRequest);
}
