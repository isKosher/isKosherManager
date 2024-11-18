package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.exception.EntityNotFoundException;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.service.BusinessService;
import com.kosher.iskosher.service.lookups.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    private final CityService cityService;

    @Override
    public List<BusinessPreviewResponse> getBusinessPreviews() {
        return businessRepository.getAllBusinesses();
    }

    @Override
    public BusinessDetailedResponse getBusinessDetails(UUID id) {
        return businessRepository.getBusinessDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", id));
    }

    @Override
    public void deleteBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", id));
        businessRepository.delete(business);
       // log.info("Successfully deleted business with ID: {}", id);
    }
    public BusinessDetailedResponse mapToDetailedResponse(Business business) {
        return null;
    }
}
