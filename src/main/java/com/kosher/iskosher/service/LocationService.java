package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.entity.Address;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.entity.Location;

public interface LocationService {
    Location createLocation(BusinessCreateRequest dto, City city, Address address);
}
