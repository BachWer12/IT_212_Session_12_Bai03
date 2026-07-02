package com.abcbank.ekyc.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CitizenIdValidator implements ConstraintValidator<ValidCitizenId, String> {

    private static final String CITIZEN_ID_REGEX = "^\\d{12}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.matches(CITIZEN_ID_REGEX);
    }
}
