package com.mongodb.api.hrms.validation;

import com.mongodb.api.hrms.dto.LeaveDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LeaveDatesValidator implements ConstraintValidator<ValidLeaveDates, LeaveDto> {
    @Override
    public boolean isValid(LeaveDto value, ConstraintValidatorContext context) {
        return value == null ||
                value.getStartDate() == null ||
                value.getEndDate() == null ||
                !value.getStartDate().isAfter(value.getEndDate());
    }
}
