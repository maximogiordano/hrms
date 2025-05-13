package com.mongodb.api.hrms.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    private String jobId;
    private BigDecimal salary;
    private String managerId;
    private List<Address> addresses;
    private Map<String, Integer> leaveBalance;
    private List<Leave> leaves;
}
