package com.kosher.iskosher.configuration;


import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("@businessService.isBusinessOwnedByUser(#businessId, authentication.principal.userId)")
public @interface IsBusinessOwner {
}