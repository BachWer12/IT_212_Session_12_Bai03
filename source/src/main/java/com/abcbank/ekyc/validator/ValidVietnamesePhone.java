package com.abcbank.ekyc.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VietnamesePhoneValidator.class)
@Documented
public @interface ValidVietnamesePhone {

    String message() default "So dien thoai khong dung dinh dang VN (10 so, bat dau bang 03/05/07/08/09)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
