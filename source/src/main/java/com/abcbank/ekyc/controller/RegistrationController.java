package com.abcbank.ekyc.controller;

import com.abcbank.ekyc.dto.RegistrationRequest;
import com.abcbank.ekyc.dto.RegistrationResponse;
import com.abcbank.ekyc.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ekyc")
@RequiredArgsConstructor
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final RegistrationService registrationService;

    /**
     * API dang ky thong tin co ban cho khach hang
     * POST /api/v1/ekyc/register
     *
     * @param request Thong tin dang ky tu client
     * @return RegistrationResponse chua registrationId de tiep tuc flow eKYC
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        log.info("Nhan request dang ky eKYC");
        long startTime = System.currentTimeMillis();

        RegistrationResponse response = registrationService.register(request);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Xu ly dang ky thanh cong trong {}ms, registrationId: {}", 
            duration, response.getRegistrationId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
