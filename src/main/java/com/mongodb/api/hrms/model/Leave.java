package com.mongodb.api.hrms.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Leave {
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String approvedBy;
}
