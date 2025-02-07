package com.kosher.iskosher.configuration;

import com.kosher.iskosher.repository.UsersBusinessRepository;
import com.kosher.iskosher.types.CustomAuthentication;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagedBusinessValidator implements ConstraintValidator<ManagedBusiness, UUID> {

    private final UsersBusinessRepository usersBusinessRepository;

    @Override
    public boolean isValid(UUID businessId, ConstraintValidatorContext context) {
        if (businessId == null) {
            log.debug("Business ID is null");
            return false;
        }

        return getUserId()
                .map(userId -> validateUserManagesBusiness(userId, businessId))
                .orElse(false);
    }

    private Optional<UUID> getUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> auth.getPrincipal() instanceof CustomAuthentication)
                .map(auth -> ((CustomAuthentication) auth.getPrincipal()).getUserId());
    }

    private boolean validateUserManagesBusiness(UUID userId, UUID businessId) {
        try {
            return usersBusinessRepository.isUserManagingBusiness(userId, businessId);
        } catch (Exception e) {
            log.warn("Error checking business management for user {} and business {}: {}", userId, businessId, e.getMessage());
            return false;
        }
    }
}
