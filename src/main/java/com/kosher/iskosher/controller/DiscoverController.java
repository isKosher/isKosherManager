package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.response.*;
import com.kosher.iskosher.service.BusinessService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/discover")
public class DiscoverController {

    private final BusinessService businessService;

    @GetMapping("/preview")
    public ResponseEntity<?> getBusinessPreviews(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "Page must be greater than or equal to 1") int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than or equal to 1")
            @Max(value = 100, message = "Size must be less than or equal to 100") int size) {

        int adjustedPage = page - 1; // Adjust to 0-based index
        Pageable pageable = PageRequest.of(adjustedPage, size);

        Page<BusinessPreviewResponse> businesses = businessService.getBusinessPreviews(pageable);

        return ResponseEntity.ok(new PageResponse<>(businesses));
    }

    @GetMapping("/filter")
    public PageResponse<BusinessPreviewResponse> filterBusinesses(
            BusinessFilterCriteria criteria,
            @PageableDefault(size = 20, sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Searching businesses with page size: {}, sort: {}", pageable.getPageSize(), pageable.getSort());
        return new PageResponse<>(businessService.filterBusinesses(criteria, pageable));
    }

    //TODO: add pageSize and pageNumber to search
    @GetMapping("/search")
    public ResponseEntity<List<BusinessSearchResponse>> searchBusinesses(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "2") int minChars) {

        if (query == null || query.length() < minChars) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(businessService.searchBusinesses(query));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BusinessDetailedResponse> getBusinessDetails(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(businessService.getBusinessDetails(id));
    }

    @GetMapping("/nearby")
    public ResponseEntity<PageResponse<BusinessPreviewTravelResponse>> getNearbyBusinesses(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "15.0") @Min(value = 0, message = "Radius must be positive")
            @Max(value = 2000, message = "Radius must be less than or equal to 50 km") double radius,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

        int adjustedPage = page - 1;
        Pageable pageable = PageRequest.of(adjustedPage, size);

        Page<BusinessPreviewTravelResponse> businesses =
                businessService.getNearbyBusinesses(lat, lon, radius, pageable);

        return ResponseEntity.ok(new PageResponse<>(businesses));
    }
}
