package com.abcbank.ekyc.service.impl;

import com.abcbank.ekyc.dto.RegistrationRequest;
import com.abcbank.ekyc.dto.RegistrationResponse;
import com.abcbank.ekyc.entity.Registration;
import com.abcbank.ekyc.entity.RegistrationStatus;
import com.abcbank.ekyc.exception.DuplicateResourceException;
import com.abcbank.ekyc.repository.RegistrationRepository;
import com.abcbank.ekyc.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final RegistrationRepository registrationRepository;

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        log.info("Bat dau dang ky thong tin cho phone: {}, email: {}", 
            maskPhone(request.getPhone()), maskEmail(request.getEmail()));

        // Kiểm tra trùng lặp citizenId
        if (registrationRepository.existsByCitizenId(request.getCitizenId())) {
            log.warn("CCCD {} da ton tai trong he thong", maskCitizenId(request.getCitizenId()));
            throw new DuplicateResourceException("citizenId", 
                "So CCCD da duoc dang ky trong he thong");
        }

        // Kiểm tra trùng lặp phone
        if (registrationRepository.existsByPhone(request.getPhone())) {
            log.warn("So dien thoai {} da ton tai", maskPhone(request.getPhone()));
            throw new DuplicateResourceException("phone", 
                "So dien thoai da duoc dang ky");
        }

        // Kiểm tra trùng lặp email
        if (registrationRepository.existsByEmail(request.getEmail())) {
            log.warn("Email {} da ton tai", maskEmail(request.getEmail()));
            throw new DuplicateResourceException("email", 
                "Email da duoc dang ky");
        }

        // Tạo entity Registration mới
        Registration registration = Registration.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .citizenId(request.getCitizenId())
                .status(RegistrationStatus.PENDING_REGISTRATION)
                .build();

        registration = registrationRepository.save(registration);
        log.info("Dang ky thanh cong voi registrationId: {}", registration.getId());

        return RegistrationResponse.builder()
                .registrationId(registration.getId())
                .message("Dang ky thong tin thanh cong")
                .nextStep("OCR_VERIFICATION")
                .build();
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) return "***";
        return phone.substring(0, 4) + "***" + phone.substring(phone.length() - 3);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        int atIndex = email.indexOf("@");
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }

    private String maskCitizenId(String citizenId) {
        if (citizenId == null || citizenId.length() < 8) return "***";
        return citizenId.substring(0, 4) + "****" + citizenId.substring(citizenId.length() - 4);
    }
}
