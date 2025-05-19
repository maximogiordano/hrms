package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.exception.DocumentNotFoundException;
import com.mongodb.api.hrms.exception.DocumentOperationException;
import com.mongodb.api.hrms.mapper.LeaveMapper;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.model.Leave;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.repository.LeaveRepository;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {
    @InjectMocks
    LeaveService leaveService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    LeaveRepository leaveRepository;

    @Mock
    LeaveMapper leaveMapper;

    @Test
    void createLeaveSuccessfully() {
        String id = "68294342f43b2332e73fbb07";

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        Leave inputAsEntity = TestDataUtils.createFullyPopulatedLeave(null);
        Leave outputAsEntity = TestDataUtils.createFullyPopulatedLeave(id);
        LeaveDto output = TestDataUtils.createFullyPopulatedLeaveDto(id);
        Employee employee = TestDataUtils.createFullyPopulatedEmployee(input.getEmployeeId());

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(leaveMapper.leaveDtoToLeave(input)).thenReturn(inputAsEntity);
        when(leaveRepository.save(inputAsEntity)).thenReturn(outputAsEntity);
        when(leaveMapper.leaveToLeaveDto(outputAsEntity)).thenReturn(output);

        LeaveDto result = leaveService.createLeave(input);

        verify(employeeRepository).findById(employee.getId());
        verify(leaveMapper).leaveDtoToLeave(input);
        verify(leaveRepository).save(inputAsEntity);
        verify(employeeRepository).decrementLeaveBalance(employee.getId(), input.getLeaveType(), 5);
        verify(leaveMapper).leaveToLeaveDto(outputAsEntity);

        assertEquals(output, result);
    }

    @Test
    void createLeaveWithEmployeeNotFound() {
        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);

        when(employeeRepository.findById(input.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> leaveService.createLeave(input), "employee not found");

        verify(employeeRepository).findById(input.getEmployeeId());
    }

    @Test
    void createLeaveWithInvalidBalance() {
        LocalDate endDate = LocalDate.of(2025, 6, 19);

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        input.setEndDate(endDate);

        Leave inputAsEntity = TestDataUtils.createFullyPopulatedLeave(null);
        inputAsEntity.setEndDate(endDate);

        Employee employee = TestDataUtils.createFullyPopulatedEmployee(input.getEmployeeId());

        when(employeeRepository.findById(input.getEmployeeId())).thenReturn(Optional.of(employee));
        when(leaveMapper.leaveDtoToLeave(input)).thenReturn(inputAsEntity);

        assertThrows(DocumentOperationException.class, () -> leaveService.createLeave(input), "insufficient balance");

        verify(employeeRepository).findById(input.getEmployeeId());
        verify(leaveMapper).leaveDtoToLeave(input);
    }

    @Test
    void createLeaveWithInvalidApprovedBy() {
        String approvedBy = "682904c2ad128f5295905416";

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        input.setApprovedBy(approvedBy);

        Leave inputAsEntity = TestDataUtils.createFullyPopulatedLeave(null);
        inputAsEntity.setApprovedBy(approvedBy);

        Employee employee = TestDataUtils.createFullyPopulatedEmployee(input.getEmployeeId());

        when(employeeRepository.findById(input.getEmployeeId())).thenReturn(Optional.of(employee));
        when(leaveMapper.leaveDtoToLeave(input)).thenReturn(inputAsEntity);

        assertThrows(DocumentOperationException.class, () -> leaveService.createLeave(input),
                "approved_by must be employee.manager_id");

        verify(employeeRepository).findById(approvedBy);
        verify(leaveMapper).leaveDtoToLeave(input);
    }
}
