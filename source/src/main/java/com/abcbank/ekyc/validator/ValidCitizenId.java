package com.abcbank.ekyc.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CitizenIdValidator.class)
@Documented
public @interface ValidCitizenId {

    String message() default "So CCCD phai gom dung 12 chu so";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
