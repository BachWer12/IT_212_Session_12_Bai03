package com.abcbank.ekyc.entity;

public enum RegistrationStatus {
    PENDING_REGISTRATION,
    OCR_COMPLETED,
    FACE_VERIFIED,
    AWAITING_ACTIVATION,
    ACTIVE,
    REJECTED,
    EXPIRED
}
