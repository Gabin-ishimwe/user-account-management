package com.app.validation.phoneNumberValidation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if(phoneNumber == null) {
            return true;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, null);
            String regionCode = phoneNumberUtil.getRegionCodeForNumber(parsedNumber);
            return phoneNumberUtil.isValidNumber(parsedNumber) && regionCode != null;
        } catch (NumberParseException e) {
            return false;
        }
    }
}
