package com.kosher.iskosher.controller;

import com.kosher.iskosher.configuration.ManagedBusiness;
import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.service.KosherCertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/certificates")
public class CertificateAdminController {

    private final KosherCertificateService kosherCertificateService;

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<KosherCertificateDto>> getCertificate(@PathVariable @ManagedBusiness UUID businessId) {
        return ResponseEntity.ok(kosherCertificateService.getCertificates(businessId));
    }

    @PostMapping("/business/{businessId}")
    public ResponseEntity<KosherCertificateDto> createCertificate(
            @PathVariable @ManagedBusiness UUID businessId,
            @Valid @RequestBody KosherCertificateDto certificateDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(kosherCertificateService.createAndLinkCertificate(businessId, certificateDto));
    }

    @PutMapping("/business/{businessId}")
    public ResponseEntity<KosherCertificateDto> updateCertificate(
            @PathVariable @ManagedBusiness UUID businessId,
            @Valid @RequestBody KosherCertificateDto certificateDto) {
        return ResponseEntity.ok(kosherCertificateService.updateCertificate(businessId, certificateDto));
    }

    @DeleteMapping("/business/{businessId}/{certificateId}")
    public ResponseEntity<Void> deleteCertificate(
            @PathVariable @ManagedBusiness UUID businessId,
            @PathVariable UUID certificateId) {
        kosherCertificateService.deleteCertificate(businessId, certificateId);
        return ResponseEntity.noContent().build();
    }
}
