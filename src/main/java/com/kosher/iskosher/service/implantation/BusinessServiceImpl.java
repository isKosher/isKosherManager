package com.kosher.iskosher.service.implantation;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.exception.BusinessNotFoundException;
import com.kosher.iskosher.model.mappers.BusinessMapper;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;


    @Override
    public List<BusinessDto> getAllBusiness() {
        List<Business> businessList = businessRepository.findAll();
        return businessList.stream().map(BusinessMapper::businessToDto).collect(Collectors.toList());

    }

    @Override
    public List<BusinessPreviewResponse> getBusinessPreviews() {
        return businessRepository.getAllBusinesses();
    }

    @Override
    public BusinessDetailedResponse getBusinessDetails(UUID id) {
        return businessRepository.findById(id)

              .map(this::mapToDetailedResponse)
                .orElseThrow(() -> new BusinessNotFoundException(id));
    }

    @Override
    public void deleteBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new BusinessNotFoundException(id));
        businessRepository.delete(business);
       // log.info("Successfully deleted business with ID: {}", id);
    }
    public BusinessDetailedResponse mapToDetailedResponse(Business business) {
        return BusinessDetailedResponse.builder()
                .businessName(business.getName())
                .build();
    }
}
