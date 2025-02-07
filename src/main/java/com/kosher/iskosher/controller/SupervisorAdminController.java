package com.kosher.iskosher.controller;

import com.kosher.iskosher.configuration.ManagedBusiness;
import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.service.KosherSupervisorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/supervisors")
public class SupervisorAdminController {

    private final KosherSupervisorService kosherSupervisorService;

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<KosherSupervisorDto>> getSupervisors(@PathVariable @ManagedBusiness UUID businessId) {
        return ResponseEntity.ok(kosherSupervisorService.getSupervisors(businessId));
    }

    @PostMapping("/business/{businessId}/add")
    public ResponseEntity<KosherSupervisorDto> createAndLinkSupervisor(
            @PathVariable @ManagedBusiness UUID businessId,
            @Valid @RequestBody KosherSupervisorDto supervisorDto) {

        KosherSupervisorDto createdSupervisor = kosherSupervisorService.createAndLinkSupervisor(businessId,
                supervisorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupervisor);
    }

    @PutMapping("/business/{businessId}")
    public ResponseEntity<KosherSupervisorDto> updateSupervisor(
            @PathVariable @ManagedBusiness UUID businessId,
            @Valid @RequestBody KosherSupervisorDto supervisorDto) {
        return ResponseEntity.ok(
                kosherSupervisorService.updateSupervisor(businessId, supervisorDto));
    }

    @DeleteMapping("/{businessId}/{supervisorId}")
    public ResponseEntity<Void> removeSupervisor(
            @PathVariable @ManagedBusiness UUID businessId,
            @PathVariable UUID supervisorId) {
        kosherSupervisorService.deleteSupervisorFromBusiness(businessId, supervisorId);
        return ResponseEntity.noContent().build();
    }
}
