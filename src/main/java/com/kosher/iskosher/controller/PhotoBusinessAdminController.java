package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.service.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/photos")
public class PhotoBusinessAdminController {

    private final PhotoService photoService;

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<BusinessPhotoDto>> getPhotosByBusiness(
            @PathVariable UUID businessId
    ) {
        return ResponseEntity.ok(photoService.getBusinessPhotos(businessId));
    }

    @PostMapping("/business/{businessId}")
    public ResponseEntity<BusinessPhotoDto> createPhoto(
            @PathVariable UUID businessId,
            @RequestBody @Valid BusinessPhotoDto dto
    ) {
        BusinessPhotoDto created = photoService.createBusinessPhoto(businessId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping()
    public ResponseEntity<BusinessPhotoDto> updatePhoto(
            @RequestBody @Valid BusinessPhotoDto dto
    ) {
        return ResponseEntity.ok(photoService.updateBusinessPhoto(dto));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhotoFromBusiness(
            @PathVariable UUID photoId
    ) {
        photoService.deletePhotoFromBusiness(photoId);
        return ResponseEntity.noContent().build();
    }

}



