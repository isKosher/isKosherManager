package com.kosher.iskosher.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosher.iskosher.configuration.jwt.JwtProvider;
import com.kosher.iskosher.dto.response.ErrorResponse;
import com.kosher.iskosher.types.CustomAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtService;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtService.validateAccessToken(token)) {
                    String email = jwtService.extractEmailFromAccessToken(token);
                    UUID userId = jwtService.extractUserIdFromAccessToken(token);

                    CustomAuthentication authentication = new CustomAuthentication(email, userId);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    authentication,
                                    null,
                                    authentication.getAuthorities()
                            )
                    );
                }
            }
            chain.doFilter(request, response);

        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message("Authentication failed")
                    .error(e.getMessage())
                    .path(request.getRequestURI())
                    .timestamp(Instant.now())
                    .build();

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
