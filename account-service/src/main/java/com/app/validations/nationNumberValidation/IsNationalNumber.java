package com.app.validations.nationNumberValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NationalNumberValidator.class)
@Documented
public @interface IsNationalNumber {
    String message() default "Invalid 16-digit number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
