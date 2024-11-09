package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.model.mappers.BusinessMapper;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.service.BusinessService;
import com.kosher.iskosher.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/business")
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessRepository businessRepository;
    private final UserService userService;


    public BusinessController(BusinessService businessService, BusinessRepository businessRepository,
                              UserService userService) {
        this.businessService = businessService;
        this.businessRepository = businessRepository;
        this.userService = userService;
    }


    @GetMapping()
    public List<BusinessDto> get(){
        return businessService.getAllBusiness();
    }
    @GetMapping("/a")
    public List<BusinessDto> gset(){
        List<Business> allActiveBusinessesWithDetails = businessRepository.findAllActiveBusinessesWithDetails();
        return allActiveBusinessesWithDetails.stream().map(BusinessMapper::businessToDto).collect(Collectors.toList());

    }
}
