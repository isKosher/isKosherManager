package com.kosher.iskosher.service.implantation;

import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.model.mappers.BusinessMapper;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public List<BusinessDto> getAllBusiness() {
        List<Business> businessList = businessRepository.findAll();
       return businessList.stream().map(BusinessMapper::businessToDto).collect(Collectors.toList());

    }
}
