package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.LocationDto;
import com.kosher.iskosher.entity.Address;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.entity.Location;
import com.kosher.iskosher.exception.EntityNotFoundException;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.repository.LocationRepository;
import com.kosher.iskosher.repository.lookups.CityRepository;
import com.kosher.iskosher.service.LocationService;
import com.kosher.iskosher.service.lookups.AddressService;
import com.kosher.iskosher.service.lookups.CityService;
import com.kosher.iskosher.types.mappers.LocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final BusinessRepository businessRepository;
    private final CityRepository cityRepository;
    private final CityService cityService;
    private final AddressService addressService;


    // TODO: 2/7/2025 Impl to location CRUD like certificate...

    //TODO: Move save city and address here, pass only locationDto...
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

    @Transactional
    public LocationDto updateLocation(UUID businessId, LocationDto locationDto) {
        Business business = locationRepository.findByIdWithLocation(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", businessId));
        Location location = business.getLocation();
        location.setCity(cityService.createOrGetCity(locationDto.city(), locationDto.region()));
        location.setAddress(addressService.getOrCreateEntity(locationDto.address()));
        location.setLongitude(locationDto.longitude());
        location.setLatitude(locationDto.latitude());
        location.setDetails(locationDto.locationDetails());
        location.setStreetNumber(locationDto.streetNumber());
        business.setLocation(location);
        return LocationMapper.INSTANCE.toDTO(businessRepository.save(business).getLocation());
    }

}
