package com.kosher.iskosher.controller;

import com.kosher.iskosher.dto.request.RefreshTokenRequest;
import com.kosher.iskosher.dto.response.AuthResponse;
import com.kosher.iskosher.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthResponse> login(@RequestHeader("Authorization") String idToken) {
        return ResponseEntity.ok(authService.loginWithGoogle(idToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.refreshToken()));
    }

}
