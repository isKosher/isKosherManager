package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.response.*;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.Region;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.service.BusinessService;
import com.kosher.iskosher.service.lookups.RegionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final RegionService regionService;

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

    @GetMapping("/filter")
    public PageResponse<BusinessPreviewResponse> searchBusinesses(
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
        System.out.println(query);
        return ResponseEntity.ok(businessService.searchBusinesses(query));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BusinessDetailedResponse> getBusinessDetails(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(businessService.getBusinessDetails(id));
    }

    //TODO: finish isActive to false...
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(
            @PathVariable @NotNull UUID id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<BusinessCreateResponse> createBusiness(@RequestBody @Valid BusinessCreateRequest dto) {
        BusinessCreateResponse result = businessService.createBusiness(dto);
        return ResponseEntity.ok(result);
    }
}
