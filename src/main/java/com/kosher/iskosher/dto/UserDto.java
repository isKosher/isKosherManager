package com.kosher.iskosher.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
public record UserDto(
        UUID id,
        String googleId,
        String name,
        String email,
        Boolean isManager,
        List<BusinessDto> businesses
        ) {}