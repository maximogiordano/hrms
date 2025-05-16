package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.exception.DocumentNotFoundException;
import com.mongodb.api.hrms.exception.DocumentOperationException;
import com.mongodb.api.hrms.mapper.LeaveMapper;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.model.Leave;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.repository.LeaveRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.temporal.ChronoUnit;
import java.util.Map;
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
        Employee employee = findEmployeeByIdOrFail(leaveDto.getEmployeeId());
        Leave leave = leaveMapper.leaveDtoToLeave(leaveDto);

        int balance = getBalance(employee.getLeaveBalances(), leave.getLeaveType());
        int numberOfDays = getNumberOfDays(leave);

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

    private int getBalance(Map<String, Integer> leaveBalances, String leaveType) {
        return leaveBalances == null ? 0 : leaveBalances.getOrDefault(leaveType, 0);
    }

    private int getNumberOfDays(Leave leaveDto) {
        return Math.toIntExact(ChronoUnit.DAYS.between(leaveDto.getStartDate(), leaveDto.getEndDate()) + 1);
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
