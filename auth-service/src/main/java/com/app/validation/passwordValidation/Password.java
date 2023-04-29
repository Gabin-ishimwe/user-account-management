package com.app.validation.passwordValidation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface Password {

    String message() default "Invalid password!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}