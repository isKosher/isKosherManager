package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessSearchCriteria;
import com.kosher.iskosher.dto.response.*;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.Region;
import com.kosher.iskosher.model.mappers.BusinessMapper;
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
    private final BusinessRepository businessRepository;
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

    @GetMapping("/findAllTest")
    public List<BusinessDto> findAllActiveBusinessesWithDetails() {
        List<Business> allActiveBusinessesWithDetails = businessRepository.findAllActiveBusinessesWithDetails();
        return allActiveBusinessesWithDetails.stream().map(BusinessMapper::businessToDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public PageResponse<BusinessPreviewResponse> searchBusinesses(
            BusinessSearchCriteria criteria,
            @PageableDefault(size = 20, sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Searching businesses with page size: {}, sort: {}", pageable.getPageSize(), pageable.getSort());
        return new PageResponse<>(businessService.searchBusinesses(criteria, pageable));
    }

    //TODO: add pageSize and pageNumber to quickSearch
    @GetMapping("/quick-search")
    public PageResponse<BusinessQuickSearchResponse> quickSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        return new PageResponse<>(businessService.quickSearchByName(query, limit));
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

    @PostMapping("/t")
    public ResponseEntity<?> creatfeBusiness() {
        Region region = regionService.getOrCreateEntity("בדיקה");
        return ResponseEntity.ok(region);
    }


    @PostMapping()
    public ResponseEntity<BusinessCreateResponse> createBusiness(@RequestBody @Valid BusinessCreateRequest dto) {
        BusinessCreateResponse result = businessService.createBusiness(dto);
        return ResponseEntity.ok(result);
    }
}
