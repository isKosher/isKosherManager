package com.kosher.iskosher.controller;

import com.kosher.iskosher.common.lookup.LookupController;
import com.kosher.iskosher.configuration.CurrentUser;
import com.kosher.iskosher.dto.CityDto;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.response.BusinessCreateResponse;
import com.kosher.iskosher.entity.City;
import com.kosher.iskosher.service.BusinessService;
import com.kosher.iskosher.service.UserService;
import com.kosher.iskosher.service.lookups.CityService;
import com.kosher.iskosher.types.CustomAuthentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final BusinessService businessService;

    @GetMapping("/test")
    public String getCurrentUserName(@CurrentUser CustomAuthentication currentUser) {
        System.out.println(currentUser);
        if (currentUser != null) {
            return "Hello, " + currentUser.getName();
        } else {
            return "No user is logged in.";
        }
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