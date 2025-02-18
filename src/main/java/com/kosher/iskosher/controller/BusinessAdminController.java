package com.kosher.iskosher.controller;

import com.kosher.iskosher.configuration.CurrentUser;
import com.kosher.iskosher.configuration.ManagedBusiness;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessUpdateRequest;
import com.kosher.iskosher.dto.response.BusinessResponse;
import com.kosher.iskosher.dto.response.UserOwnedBusinessResponse;
import com.kosher.iskosher.service.BusinessService;
import com.kosher.iskosher.service.UserService;
import com.kosher.iskosher.types.CustomAuthentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/businesses")
public class BusinessAdminController {

    private final UserService userService;
    private final BusinessService businessService;

    @GetMapping("/my-businesses")
    public ResponseEntity<?> getOwnedBusinesses(@CurrentUser CustomAuthentication currentUser) {
        List<UserOwnedBusinessResponse> businessResponses =
                userService.getBusinessDetailsByUserId(currentUser.getUserId());

        return ResponseEntity.ok(businessResponses);
    }

    @PutMapping("/update-business")
    public ResponseEntity<BusinessResponse> updateBusiness(@CurrentUser CustomAuthentication currentUser,
                                                           @RequestBody @Valid BusinessUpdateRequest dto) {
        return ResponseEntity.ok(businessService.updateBusiness(currentUser.getUserId(), dto));
    }

    @PostMapping("create-business")
    public ResponseEntity<BusinessResponse> createBusiness(@CurrentUser CustomAuthentication currentUser,
                                                           @RequestBody @Valid BusinessCreateRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(businessService.createBusiness(currentUser.getUserId(), dto));
    }

    //TODO: finish isActive to false and valid business by userId...
    @DeleteMapping("delete-business/{id}")
    public ResponseEntity<Void> deleteBusiness(
            @PathVariable @NotNull @ManagedBusiness UUID id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}