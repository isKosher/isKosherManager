package com.kosher.iskosher.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.kosher.iskosher.configuration.jwt.JwtProvider;
import com.kosher.iskosher.configuration.jwt.JwtProviderImpl;
import com.kosher.iskosher.dto.response.AuthResponse;
import com.kosher.iskosher.exception.JwtValidationException;
import com.kosher.iskosher.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider tokenProvider;

    private final AuthService authService;


    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<?> login(@RequestHeader("Authorization") String idToken) {
        return ResponseEntity.ok(authService.loginWithGoogle(idToken));
    }

    @GetMapping ("/")
    public String home(@RequestHeader("x") String token) {

       if (tokenProvider.validateAccessToken(token)){
           String s = tokenProvider.extractEmailFromAccessToken(token);
           UUID uuid = tokenProvider.extractUserIdFromAccessToken(token);
           return s + uuid;
       }

       return null;
    }
}
