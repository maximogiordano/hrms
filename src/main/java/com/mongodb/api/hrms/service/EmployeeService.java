package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.mapper.EmployeeMapper;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

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
        return employeeRepository.findByFirstNameContainingOrLastNameContaining(name, name)
                .stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .toList();
    }
}
