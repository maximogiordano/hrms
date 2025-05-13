package com.mongodb.api.hrms.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LeaveDto {
    @NotNull(message = "leave_type must not be null")
    private String leaveType;

    @NotNull(message = "start_date must not be null")
    private LocalDate startDate;

    @NotNull(message = "end_date must not be null")
    private LocalDate endDate;

    private String status;

    private String approvedBy;
}
