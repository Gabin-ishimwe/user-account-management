package com.app.validations.maritalStatusEnumValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaritalStatusEnumValidatorConstraint implements ConstraintValidator<MaritalStatusEnumValidator, String> {

    Set<String> values;

    @Override
    public void initialize(MaritalStatusEnumValidator constraintAnnotation) {
        values = Stream.of(constraintAnnotation.enumC().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return values.contains(value);
    }
}