package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.exception.EntityNotFoundException;
import com.mongodb.api.hrms.exception.EntityOperationException;
import com.mongodb.api.hrms.mapper.EmployeeMapper;
import com.mongodb.api.hrms.mapper.LeaveMapper;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final LeaveMapper leaveMapper;

    public EmployeeDto createEmployee(@Valid EmployeeDto employeeDto) {
        var employee = employeeMapper.employeeDtoToEmployee(employeeDto);

        Optional<Employee> searchedEmployee = employeeRepository.findByFirstNameAndLastNameAndPhoneNumber(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber()
        );

        if (searchedEmployee.isPresent()) {
            employee.setId(searchedEmployee.get().getId());
        }

        employee = employeeRepository.save(employee);

        return employeeMapper.employeeToEmployeeDto(employee);
    }

    public List<EmployeeDto> searchEmployeeByName(String name) {
        return employeeRepository.searchEmployeeByName(name)
                .stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .toList();
    }

    public void createLeave(String employeeId, @Valid LeaveDto leaveDto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("employee not found"));

        int balance = employee.getLeaveBalance().getOrDefault(leaveDto.getLeaveType(), 0);
        int numberOfDays = (int) (ChronoUnit.DAYS.between(leaveDto.getStartDate(), leaveDto.getEndDate()) + 1);

        if (numberOfDays > balance) {
            throw new EntityOperationException("insufficient balance");
        }

        if (!Objects.equals(leaveDto.getApprovedBy(), employee.getManagerId())) {
            throw new EntityOperationException("invalid manager id");
        }

        var leave = leaveMapper.leaveDtoToLeave(leaveDto);

        employee.getLeaves().add(leave);
        employee.getLeaveBalance().put(leaveDto.getLeaveType(), balance - numberOfDays);
        employeeRepository.save(employee);
    }
}
