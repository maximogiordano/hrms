package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.mapper.EmployeeMapper;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmployeeMapper employeeMapper;

    @Test
    void createExistingEmployee() {
        String id = "682904c2ad128f5295905416";

        EmployeeDto input = TestDataUtils.createFullyPopulatedEmployeeDto(null);
        Employee inputAsEntity = TestDataUtils.createFullyPopulatedEmployee(null);
        Employee foundEmployee = TestDataUtils.createAnotherFullyPopulatedEmployee(id);
        Employee employeeToUpdate = TestDataUtils.createFullyPopulatedEmployee(id);
        Employee savedEmployee = TestDataUtils.createFullyPopulatedEmployee(id);
        EmployeeDto savedEmployeeDto = TestDataUtils.createFullyPopulatedEmployeeDto(id);

        when(employeeMapper.employeeDtoToEmployee(input)).thenReturn(inputAsEntity);
        when(employeeRepository.findByFirstNameAndLastNameAndPhoneNumber(input.getFirstName(), input.getLastName(),
                input.getPhoneNumber())).thenReturn(Optional.of(foundEmployee));
        when(employeeRepository.save(employeeToUpdate)).thenReturn(savedEmployee);
        when(employeeMapper.employeeToEmployeeDto(savedEmployee)).thenReturn(savedEmployeeDto);

        EmployeeDto output = employeeService.createEmployee(input);

        verify(employeeMapper).employeeDtoToEmployee(input);
        verify(employeeRepository).findByFirstNameAndLastNameAndPhoneNumber(input.getFirstName(), input.getLastName(),
                input.getPhoneNumber());
        verify(employeeRepository).save(employeeToUpdate);
        verify(employeeMapper).employeeToEmployeeDto(savedEmployee);

        assertEquals(savedEmployeeDto, output);
    }


    @Test
    void createNewEmployee() {
        String id = "682904c2ad128f5295905416";

        EmployeeDto input = TestDataUtils.createFullyPopulatedEmployeeDto(null);
        Employee inputAsEntity = TestDataUtils.createFullyPopulatedEmployee(null);
        Employee savedEmployee = TestDataUtils.createFullyPopulatedEmployee(id);
        EmployeeDto savedEmployeeDto = TestDataUtils.createFullyPopulatedEmployeeDto(id);

        when(employeeMapper.employeeDtoToEmployee(input)).thenReturn(inputAsEntity);
        when(employeeRepository.findByFirstNameAndLastNameAndPhoneNumber(input.getFirstName(), input.getLastName(),
                input.getPhoneNumber())).thenReturn(Optional.empty());
        when(employeeRepository.save(inputAsEntity)).thenReturn(savedEmployee);
        when(employeeMapper.employeeToEmployeeDto(savedEmployee)).thenReturn(savedEmployeeDto);

        EmployeeDto output = employeeService.createEmployee(input);

        verify(employeeMapper).employeeDtoToEmployee(input);
        verify(employeeRepository).findByFirstNameAndLastNameAndPhoneNumber(input.getFirstName(), input.getLastName(),
                input.getPhoneNumber());
        verify(employeeRepository).save(inputAsEntity);
        verify(employeeMapper).employeeToEmployeeDto(savedEmployee);

        assertEquals(savedEmployeeDto, output);
    }

    @Test
    void searchEmployeeByName() {
        String id = "682904c2ad128f5295905416";

        Employee employee = TestDataUtils.createFullyPopulatedEmployee(id);
        EmployeeDto employeeDto = TestDataUtils.createFullyPopulatedEmployeeDto(id);

        String name = employee.getFirstName();
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName", "firstName", "phoneNumber"));
        Page<Employee> employees = new PageImpl<>(List.of(employee), pageable, 1);
        Page<EmployeeDto> employeesDto = new PageImpl<>(List.of(employeeDto), pageable, 1);

        when(employeeRepository.findByName(name, pageable)).thenReturn(employees);
        when(employeeMapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        Page<EmployeeDto> result = employeeService.searchEmployeeByName(name, page, size);

        verify(employeeRepository).findByName(name, pageable);
        verify(employeeMapper).employeeToEmployeeDto(employee);

        assertEquals(employeesDto, result);
    }
}
