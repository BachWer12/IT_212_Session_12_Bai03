package com.abcbank.ekyc.service;

import com.abcbank.ekyc.dto.RegistrationRequest;
import com.abcbank.ekyc.dto.RegistrationResponse;
import com.abcbank.ekyc.entity.Registration;
import com.abcbank.ekyc.entity.RegistrationStatus;
import com.abcbank.ekyc.exception.DuplicateResourceException;
import com.abcbank.ekyc.repository.RegistrationRepository;
import com.abcbank.ekyc.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private RegistrationRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = RegistrationRequest.builder()
                .fullName("Nguyễn Văn An")
                .phone("0987654321")
                .email("nguyenvanan@gmail.com")
                .citizenId("001202012345")
                .build();
    }

    @Test
    void register_ShouldReturnResponse_WhenDataIsValid() {
        when(registrationRepository.existsByCitizenId(anyString())).thenReturn(false);
        when(registrationRepository.existsByPhone(anyString())).thenReturn(false);
        when(registrationRepository.existsByEmail(anyString())).thenReturn(false);

        Registration savedRegistration = Registration.builder()
                .id("uuid-123")
                .fullName("Nguyễn Văn An")
                .phone("0987654321")
                .email("nguyenvanan@gmail.com")
                .citizenId("001202012345")
                .status(RegistrationStatus.PENDING_REGISTRATION)
                .build();

        when(registrationRepository.save(any(Registration.class))).thenReturn(savedRegistration);

        RegistrationResponse response = registrationService.register(validRequest);

        assertNotNull(response);
        assertEquals("uuid-123", response.getRegistrationId());
        assertEquals("Đăng ký thông tin thành công", response.getMessage());
        assertEquals("OCR_VERIFICATION", response.getNextStep());

        verify(registrationRepository, times(1)).existsByCitizenId("001202012345");
        verify(registrationRepository, times(1)).existsByPhone("0987654321");
        verify(registrationRepository, times(1)).existsByEmail("nguyenvanan@gmail.com");
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void register_ShouldThrowException_WhenCitizenIdExists() {
        when(registrationRepository.existsByCitizenId("001202012345")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> registrationService.register(validRequest)
        );

        assertEquals("citizenId", exception.getField());
        assertEquals("Số CCCD đã được đăng ký trong hệ thống", exception.getMessage());

        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void register_ShouldThrowException_WhenPhoneExists() {
        when(registrationRepository.existsByCitizenId(anyString())).thenReturn(false);
        when(registrationRepository.existsByPhone("0987654321")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> registrationService.register(validRequest)
        );

        assertEquals("phone", exception.getField());
        assertEquals("Số điện thoại đã được đăng ký", exception.getMessage());

        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        when(registrationRepository.existsByCitizenId(anyString())).thenReturn(false);
        when(registrationRepository.existsByPhone(anyString())).thenReturn(false);
        when(registrationRepository.existsByEmail("nguyenvanan@gmail.com")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> registrationService.register(validRequest)
        );

        assertEquals("email", exception.getField());
        assertEquals("Email đã được đăng ký", exception.getMessage());

        verify(registrationRepository, never()).save(any(Registration.class));
    }
}
