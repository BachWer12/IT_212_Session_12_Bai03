package com.abcbank.ekyc.service;

import com.abcbank.ekyc.dto.RegistrationRequest;
import com.abcbank.ekyc.dto.RegistrationResponse;

/**
 * Service xử lý nghiệp vụ đăng ký tài khoản eKYC
 */
public interface RegistrationService {

    /**
     * Đăng ký thông tin cơ bản cho khách hàng mới
     *
     * @param request Thông tin đăng ký (fullName, phone, email, citizenId)
     * @return RegistrationResponse chứa registrationId và nextStep
     */
    RegistrationResponse register(RegistrationRequest request);
}
