package com.kosher.iskosher.configuration.jwt;

import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.exception.JwtValidationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProviderImpl implements JwtProvider {

    private String secret;

    private long accessTokenValidity;

    private long refreshTokenValidity;
    private String issuer;
    private String audience;

    private final Key SECRET_KEY;


    public JwtProviderImpl(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.access-token-validity}") long accessTokenValidity,
                           @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
                           @Value("${jwt.issuer}") String issuer,
                           @Value("${jwt.audience}") String audience) {
        this.secret = secret;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.issuer = issuer;
        this.audience = audience;

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
        return generateToken(user, accessTokenValidity);
    }

    @Override
    public String generateRefreshToken(UserDto user) {
        return generateToken(user, refreshTokenValidity);
    }

    private String generateToken(UserDto user, long validity) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.email())
                .claim("user_id", user.id().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(validity)))
                .setIssuer(issuer)
                .setAudience(audience)
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
