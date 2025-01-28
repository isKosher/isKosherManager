package com.kosher.iskosher.controller;

import com.kosher.iskosher.configuration.CurrentUser;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.response.BusinessCreateResponse;
import com.kosher.iskosher.dto.response.UserOwnedBusinessResponse;
import com.kosher.iskosher.service.BusinessService;
import com.kosher.iskosher.service.UserService;
import com.kosher.iskosher.types.CustomAuthentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final BusinessService businessService;


    @GetMapping("/my-businesses")
    public ResponseEntity<?> getOwnedBusinesses(@CurrentUser CustomAuthentication currentUser) {
        List<UserOwnedBusinessResponse> businessResponses =
                userService.getBusinessDetailsByUserId(currentUser.getUserId());
        if (businessResponses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(businessResponses);
    }


    @PostMapping("create-business")
    public ResponseEntity<BusinessCreateResponse> createBusiness(@CurrentUser CustomAuthentication currentUser,
                                                                 @RequestBody @Valid BusinessCreateRequest dto) {
        return ResponseEntity.ok(businessService.createBusiness(currentUser.getUserId(), dto));
    }

    //TODO: finish isActive to false and valid business by userId...
    @DeleteMapping("delete-business/{id}")
    public ResponseEntity<Void> deleteBusiness(
            @PathVariable @NotNull UUID id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}