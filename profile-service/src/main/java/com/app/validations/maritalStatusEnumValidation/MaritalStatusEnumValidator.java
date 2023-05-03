package com.app.validations.maritalStatusEnumValidation;

import com.app.entities.MaritalStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaritalStatusEnumValidatorConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaritalStatusEnumValidator {

    Class<? extends Enum<?>> enumC() default MaritalStatus.class;
    String message() default "Invalid value. Must be any of enum {enumClass}.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}