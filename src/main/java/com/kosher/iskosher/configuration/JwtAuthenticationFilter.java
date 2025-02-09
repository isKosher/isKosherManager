package com.kosher.iskosher.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosher.iskosher.common.utils.CookieUtil;
import com.kosher.iskosher.configuration.jwt.JwtProvider;
import com.kosher.iskosher.dto.response.ErrorResponse;
import com.kosher.iskosher.types.CustomAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.private-urls}")
    private String privateUrls;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith(privateUrls);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException {
        try {
            String token = extractToken(request);

            if (token != null && jwtProvider.validateAccessToken(token)) {
                UUID userId = jwtProvider.extractUserIdFromAccessToken(token);
                String email = jwtProvider.extractEmailFromAccessToken(token);

                CustomAuthentication authentication = new CustomAuthentication(email, userId);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(authentication, null, authentication.getAuthorities())
                );
            } else {
                handleAuthenticationError(request, response, "Invalid or missing token");
                return;
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            handleAuthenticationError(request, response, e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        Optional<String> tokenFromCookie = cookieUtil.getAccessTokenFromCookie(request);
        return tokenFromCookie.orElse(null);
    }

    private void handleAuthenticationError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Authentication failed")
                .error(errorMessage)
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
