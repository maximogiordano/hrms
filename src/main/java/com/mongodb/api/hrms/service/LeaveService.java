package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.exception.DocumentNotFoundException;
import com.mongodb.api.hrms.exception.DocumentOperationException;
import com.mongodb.api.hrms.mapper.LeaveMapper;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.repository.LeaveRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class LeaveService {
    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveMapper leaveMapper;

    @Transactional
    public LeaveDto createLeave(@Valid LeaveDto leaveDto) {
        var employee = findEmployeeByIdOrFail(leaveDto.getEmployeeId());
        var leave = leaveMapper.leaveDtoToLeave(leaveDto);

        int balance = employee.getBalance(leave.getLeaveType());
        int numberOfDays = leave.getNumberOfDays();

        checkBalance(balance, numberOfDays);
        checkManager(leaveDto.getApprovedBy(), employee.getManagerId());

        leave = leaveRepository.save(leave);
        employeeRepository.decrementLeaveBalance(leave.getEmployeeId(), leave.getLeaveType(), numberOfDays);

        return leaveMapper.leaveToLeaveDto(leave);
    }

    private Employee findEmployeeByIdOrFail(String employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new DocumentNotFoundException("employee not found"));
    }

    private void checkBalance(int balance, int numberOfDays) {
        if (balance < numberOfDays) {
            throw new DocumentOperationException("insufficient balance");
        }
    }

    private void checkManager(String approvedBy, String managerId) {
        if (!Objects.equals(approvedBy, managerId)) {
            throw new DocumentOperationException("approved_by must be employee.manager_id");
        }
    }
}
