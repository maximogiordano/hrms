package com.mongodb.api.hrms.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Document(collection = "leaves")
public class Leave {
    @Id
    private String id;
    private String employeeId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String approvedBy;

    public int getNumberOfDays() {
        return Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate) + 1);
    }
}
