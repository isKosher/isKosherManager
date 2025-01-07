package com.kosher.iskosher.repository;

import com.kosher.iskosher.dto.request.BusinessSearchCriteria;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBusinessRepository {
    Page<BusinessPreviewResponse> searchBusinesses(BusinessSearchCriteria criteria, Pageable pageable);
}
