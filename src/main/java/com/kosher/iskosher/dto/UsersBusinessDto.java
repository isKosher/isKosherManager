package com.kosher.iskosher.dto;

import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.User;
import com.kosher.iskosher.entity.UsersBusiness;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link UsersBusiness}
 */
public record UsersBusinessDto(
        UUID id,
        @NotNull BusinessDto business,
        @NotNull UserDto user
) {}