package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.entity.Address;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.entity.Location;
import com.kosher.iskosher.repository.LocationRepository;
import com.kosher.iskosher.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location createLocation(BusinessCreateRequest dto, City city, Address address) {
        Location location = new Location();
        location.setCity(city);
        location.setAddress(address);
        location.setStreetNumber(dto.streetNumber());
        location.setDetails(dto.locationDetails());
        return locationRepository.save(location);
    }

}
