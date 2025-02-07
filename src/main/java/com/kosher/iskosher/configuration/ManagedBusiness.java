package com.kosher.iskosher.configuration;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ManagedBusinessValidator.class)
public @interface ManagedBusiness {
    String message() default "User does not manage this business.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}