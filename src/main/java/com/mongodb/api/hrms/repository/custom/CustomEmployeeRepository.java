package com.mongodb.api.hrms.repository.custom;

public interface CustomEmployeeRepository {
    void decrementLeaveBalance(String employeeId, String leaveType, int numberOfDays);
}
