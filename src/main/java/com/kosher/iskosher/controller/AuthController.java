package com.kosher.iskosher.controller;

import com.kosher.iskosher.common.utils.CookieUtil;
import com.kosher.iskosher.dto.response.AuthResponse;
import com.kosher.iskosher.exception.AuthenticationException;
import com.kosher.iskosher.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<?> login(@RequestHeader("Authorization") String idToken, HttpServletResponse response) {
        AuthResponse authResponse = authService.loginWithGoogle(idToken);

        cookieUtil.createAccessTokenCookie(authResponse.getAccessToken(), response);
        cookieUtil.createRefreshTokenCookie(authResponse.getRefreshToken(), response);

        return ResponseEntity.ok(authResponse.getUserId());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieUtil.deleteAccessTokenCookie(response);
        cookieUtil.deleteRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request)
                .orElseThrow(() -> new AuthenticationException("Refresh token not found"));

        AuthResponse authResponse = authService.refreshToken(refreshToken);

        cookieUtil.createAccessTokenCookie(authResponse.getAccessToken(), response);
        cookieUtil.createRefreshTokenCookie(authResponse.getRefreshToken(), response);

        return ResponseEntity.ok().build();
    }

}
