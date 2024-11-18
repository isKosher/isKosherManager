package com.kosher.iskosher.controller;

import com.kosher.iskosher.common.lookup.LookupController;
import com.kosher.iskosher.dto.CityDto;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.service.lookups.CityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/city")
public class CityController extends LookupController<City, CityDto> {
    public CityController(CityService service) {
        super(service);
    }
}