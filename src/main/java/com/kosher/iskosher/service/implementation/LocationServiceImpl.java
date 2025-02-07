package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.LocationDto;
import com.kosher.iskosher.entity.Address;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.entity.Location;
import com.kosher.iskosher.repository.LocationRepository;
import com.kosher.iskosher.repository.lookups.CityRepository;
import com.kosher.iskosher.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;

    // TODO: 2/7/2025 Impl to location CRUD like certificate...

    @Override
    public Location createLocation(LocationDto locationDto, City city, Address address) {
        if (city.getId() == null) {
            city = cityRepository.save(city);
        }
        Location location = new Location();
        location.setCity(city);
        location.setAddress(address);
        location.setStreetNumber(locationDto.streetNumber());
        location.setLatitude(locationDto.latitude());
        location.setLongitude(locationDto.longitude());
        location.setDetails(locationDto.locationDetails());
        return locationRepository.save(location);
    }

}
