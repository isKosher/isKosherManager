package com.kosher.iskosher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.model.mappers.BusinessMapper;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.repository.lookups.CityRepository;
import com.kosher.iskosher.service.BusinessService;
import com.kosher.iskosher.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Add this line
@RequestMapping(path = "/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessRepository businessRepository;

    private final CityRepository cityRepository;
    private final UserService userService;

    private final ObjectMapper objectMapper;


    @GetMapping("/preview")
    public ResponseEntity<List<BusinessPreviewResponse>> getBusinessPreviews() {
        try {
            List<BusinessPreviewResponse> businesses = businessService.getBusinessPreviews();
            if (businesses.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(businesses);
        } catch (Exception e) {
            //log.error("Error fetching businesses", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

    }

   /* @GetMapping("")
    public ResponseEntity<?> getBusinPreviews() {
        Optional<CityDto> cityDto = getCityDtoByName("ירושsלים");

        if (cityDto.isEmpty()) {
            ErrorResponse error = ErrorResponse.
                    builder()
                    .timestamp(LocalDateTime.now())
                    .message("העיר המבוקשת לא נמצאה במערכת")
                    .path("http://localhost:8080/api/v1/businesses")
                    .build();


            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(cityDto.get());
    }

    public Optional<CityDto> getCityDtoByName(String name) {
        Optional<City> city = cityRepository.findByName(name);
        return city.map(c -> new CityDto(c.getId(), c.getName()));
    }*/


    @GetMapping("/findAllTest")
    public List<BusinessDto> findAllActiveBusinessesWithDetails() {
        List<Business> allActiveBusinessesWithDetails = businessRepository.findAllActiveBusinessesWithDetails();
        return allActiveBusinessesWithDetails.stream().map(BusinessMapper::businessToDto).collect(Collectors.toList());

    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BusinessDetailedResponse> getBusinessDetails(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(businessService.getBusinessDetails(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(
            @PathVariable @NotNull UUID id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}
