package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.response.*;
import com.kosher.iskosher.service.BusinessService;
import jakarta.validation.Valid;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping("/preview")
    public ResponseEntity<?> getBusinessPreviews(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BusinessPreviewResponse> businesses = businessService.getBusinessPreviews(pageable);

            if (businesses.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            PageResponse<BusinessPreviewResponse> pageResponse = new PageResponse<>(businesses);
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
             log.error("Error fetching businesses", e);
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
