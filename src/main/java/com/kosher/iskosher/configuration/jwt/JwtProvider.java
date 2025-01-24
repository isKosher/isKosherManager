package com.kosher.iskosher.configuration.jwt;

import com.kosher.iskosher.dto.UserDto;

import java.util.UUID;

public interface JwtProvider {
    String generateAccessToken(UserDto user);

    String generateRefreshToken(UserDto user);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    String extractEmailFromAccessToken(String token);

    UUID extractUserIdFromAccessToken(String token);

}
