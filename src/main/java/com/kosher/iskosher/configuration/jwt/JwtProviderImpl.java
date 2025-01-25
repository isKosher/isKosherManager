package com.kosher.iskosher.configuration.jwt;

import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.exception.JwtValidationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProviderImpl implements JwtProvider {

    private String secret;

    private long accessTokenValidity;

    private long refreshTokenValidity;

    private final Key SECRET_KEY;


    public JwtProviderImpl(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.access-token-validity}") long accessTokenValidity,
                           @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
        this.secret = secret;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;

        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("Secret key must be at least 32 characters long for HS256.");
        }
        SECRET_KEY = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }


    public String extractEmailFromAccessToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


    public UUID extractUserIdFromAccessToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            String id = claims.get("user_id", String.class);
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new JwtValidationException("Error extracting user_id from access token", e);
        }
    }

    @Override
    public UUID extractUserIdFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            String id = claims.get("user_id", String.class);
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new JwtValidationException("Error extracting user_id from refresh token", e);
        }
    }

    @Override
    public String generateAccessToken(UserDto user) {
        return Jwts.builder()
                .setSubject(user.email())
                .claim("user_id", user.id())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDto user) {
        return Jwts.builder()
                .setSubject(user.email())
                .claim("user_id", user.id())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new JwtValidationException("Token expired", ex);
        } catch (UnsupportedJwtException ex) {
            throw new JwtValidationException("Unsupported token", ex);
        } catch (MalformedJwtException ex) {
            throw new JwtValidationException("Malformed token", ex);
        } catch (SignatureException ex) {
            throw new JwtValidationException("Invalid signature", ex);
        } catch (SecurityException ex) {
            throw new JwtValidationException("Security error", ex);
        } catch (IllegalArgumentException ex) {
            throw new JwtValidationException("Token claims string is empty", ex);
        }
    }

}
