package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.LocationDto;
import com.kosher.iskosher.entity.Address;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.entity.Location;

import java.util.UUID;

public interface LocationService {
    Location createLocation(LocationDto locationDto, City city, Address address);

    //    List<LocationDTO> getLocationsByBusinessId(UUID businessId);
    //    LocationDTO getLocationById(UUID businessId, UUID locationId);
    //    LocationDTO createLocation(UUID businessId, LocationDTO locationDTO);
        LocationDto updateLocation(UUID businessId, LocationDto locationDTO);
    //    void deleteLocation(UUID businessId, UUID locationId);
}

