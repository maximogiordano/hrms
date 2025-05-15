package com.mongodb.api.hrms.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LeaveDatesValidator.class)
public @interface ValidLeaveDates {
    String message() default "start_date must not be after end_date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
