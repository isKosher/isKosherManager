package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.LocationDto;
import com.kosher.iskosher.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/location")
public class LocationAdminController {

    private final LocationService locationService;

    @PutMapping("/{businessId}/location")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable UUID businessId,
                                                      @Valid @RequestBody LocationDto locationDto) {
        LocationDto updatedLocation = locationService.updateLocation(businessId, locationDto);
        return ResponseEntity.ok(updatedLocation);
    }

}
