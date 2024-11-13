package com.kosher.iskosher.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for {@link UsersBusiness}
 */
public record UsersBusinessDto(
        UUID id,
        @NotNull BusinessDto business,
        @NotNull UserDto user
) {}