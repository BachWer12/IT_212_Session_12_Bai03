package com.abcbank.ekyc.controller;

import com.abcbank.ekyc.dto.RegistrationRequest;
import com.abcbank.ekyc.dto.RegistrationResponse;
import com.abcbank.ekyc.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void register_ShouldReturn201_WhenDataIsValid() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .fullName("Nguyễn Văn An")
                .phone("0987654321")
                .email("nguyenvanan@gmail.com")
                .citizenId("001202012345")
                .build();

        RegistrationResponse response = RegistrationResponse.builder()
                .registrationId("uuid-123")
                .message("Đăng ký thông tin thành công")
                .nextStep("OCR_VERIFICATION")
                .build();

        when(registrationService.register(any(RegistrationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/ekyc/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationId").value("uuid-123"))
                .andExpect(jsonPath("$.message").value("Đăng ký thông tin thành công"))
                .andExpect(jsonPath("$.nextStep").value("OCR_VERIFICATION"));
    }

    @Test
    void register_ShouldReturn400_WhenFullNameIsEmpty() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .fullName("")
                .phone("0987654321")
                .email("nguyenvanan@gmail.com")
                .citizenId("001202012345")
                .build();

        mockMvc.perform(post("/ekyc/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.fullName").value("Họ và tên không được để trống"));
    }

    @Test
    void register_ShouldReturn400_WhenEmailIsInvalid() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .fullName("Nguyễn Văn An")
                .phone("0987654321")
                .email("invalid-email")
                .citizenId("001202012345")
                .build();

        mockMvc.perform(post("/ekyc/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.email").value("Email không đúng định dạng"));
    }

    @Test
    void register_ShouldReturn400_WhenCitizenIdIsInvalid() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .fullName("Nguyễn Văn An")
                .phone("0987654321")
                .email("nguyenvanan@gmail.com")
                .citizenId("12345")
                .build();

        mockMvc.perform(post("/ekyc/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.citizenId").value("Số CCCD phải gồm đúng 12 chữ số"));
    }

    @Test
    void register_ShouldReturn400_WhenPhoneIsInvalid() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .fullName("Nguyễn Văn An")
                .phone("123456")
                .email("nguyenvanan@gmail.com")
                .citizenId("001202012345")
                .build();

        mockMvc.perform(post("/ekyc/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }
}
