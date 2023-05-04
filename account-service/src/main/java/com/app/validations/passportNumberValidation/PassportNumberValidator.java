package com.app.validations.passportNumberValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PassportNumberValidator implements ConstraintValidator<IsPassportNumber, String> {

    private static final Pattern pattern = Pattern.compile("PC\\d{4}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are allowed
        }
        return pattern.matcher(value).matches();
    }
}
