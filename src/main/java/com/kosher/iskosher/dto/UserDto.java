package com.kosher.iskosher.dto;

import com.kosher.iskosher.entity.User;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
public record UserDto(
        UUID id,
        String name,
        String email,
        Boolean isManager,
        List<BusinessDto> businesses
) {}