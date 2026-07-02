package com.abcbank.ekyc.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VietnamesePhoneValidator implements ConstraintValidator<ValidVietnamesePhone, String> {

    private static final String PHONE_REGEX = "^(03|05|07|08|09)\\d{8}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.matches(PHONE_REGEX);
    }
}
