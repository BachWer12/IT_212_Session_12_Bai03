package com.abcbank.ekyc.dto;

import com.abcbank.ekyc.validator.ValidCitizenId;
import com.abcbank.ekyc.validator.ValidVietnamesePhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistrationRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ và tên phải từ 2-100 ký tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @ValidVietnamesePhone
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được quá 100 ký tự")
    private String email;

    @NotBlank(message = "Số CCCD không được để trống")
    @ValidCitizenId
    private String citizenId;
}
