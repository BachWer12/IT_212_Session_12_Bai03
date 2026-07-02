package com.abcbank.ekyc.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    private final CitizenIdValidator citizenIdValidator = new CitizenIdValidator();
    private final VietnamesePhoneValidator phoneValidator = new VietnamesePhoneValidator();

    // ========================
    // CitizenIdValidator Tests
    // ========================

    @Test
    void citizenIdValidator_ShouldReturnTrue_WhenValid12Digits() {
        assertTrue(citizenIdValidator.isValid("001202012345", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "001202012345",
        "012345678901",
        "098765432112",
        "111111111111",
        "123456789012"
    })
    void citizenIdValidator_ShouldReturnTrue_ForValidCitizenIds(String citizenId) {
        assertTrue(citizenIdValidator.isValid(citizenId, null));
    }

    @Test
    void citizenIdValidator_ShouldReturnFalse_WhenNull() {
        assertFalse(citizenIdValidator.isValid(null, null));
    }

    @Test
    void citizenIdValidator_ShouldReturnFalse_WhenEmpty() {
        assertFalse(citizenIdValidator.isValid("", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "00120201234",    // 11 digits
        "0012020123456",  // 13 digits
        "00120201234A",   // contains letter
        "abc123456789",   // starts with letters
        "0012 02012345",  // contains space
        "00120201234!",   // contains special char
        "0000000000000"   // 13 digits
    })
    void citizenIdValidator_ShouldReturnFalse_ForInvalidCitizenIds(String citizenId) {
        assertFalse(citizenIdValidator.isValid(citizenId, null));
    }

    // =============================
    // VietnamesePhoneValidator Tests
    // =============================

    @Test
    void phoneValidator_ShouldReturnTrue_WhenValidVietnamesePhone() {
        assertTrue(phoneValidator.isValid("0987654321", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0312345678",  // Viettel
        "0512345678",  // VNPT
        "0712345678",  // MobiFone
        "0812345678",  // Vietnamobile
        "0912345678",  // Viettel
    })
    void phoneValidator_ShouldReturnTrue_ForValidPhonePrefixes(String phone) {
        assertTrue(phoneValidator.isValid(phone, null));
    }

    @Test
    void phoneValidator_ShouldReturnFalse_WhenNull() {
        assertFalse(phoneValidator.isValid(null, null));
    }

    @Test
    void phoneValidator_ShouldReturnFalse_WhenEmpty() {
        assertFalse(phoneValidator.isValid("", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0212345678",   // wrong prefix 02
        "0412345678",   // wrong prefix 04
        "0612345678",   // wrong prefix 06
        "1012345678",   // wrong prefix 10
        "098765432",    // 9 digits
        "09876543210",  // 11 digits
        "0987654321a",  // contains letter
        "09876 54321",  // contains space
        "abc1234567",   // letters only
        "01234567890"   // 11 digits with 01 prefix
    })
    void phoneValidator_ShouldReturnFalse_ForInvalidPhones(String phone) {
        assertFalse(phoneValidator.isValid(phone, null));
    }
}
