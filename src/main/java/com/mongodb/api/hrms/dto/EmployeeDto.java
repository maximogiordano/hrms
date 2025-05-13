package com.mongodb.api.hrms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmployeeDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotNull(message = "first_name must not be null")
    private String firstName;

    @NotNull(message = "last_name must not be null")
    private String lastName;

    private String email;

    @NotNull(message = "phone_number must not be null")
    private String phoneNumber;

    private LocalDate hireDate;

    private String jobId;

    private BigDecimal salary;

    private String managerId;

    private List<@Valid AddressDto> addresses;

    private Map<String, @PositiveOrZero(message = "balance must be positive or zero") Integer> leaveBalance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<LeaveDto> leaves = new ArrayList<>();
}
