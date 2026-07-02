package com.abcbank.ekyc.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistrationResponse {

    private String registrationId;
    private String message;
    private String nextStep;
}
