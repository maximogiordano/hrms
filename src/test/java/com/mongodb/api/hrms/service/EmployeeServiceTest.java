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
    void searchEmployee() {
        String id = "682904c2ad128f5295905416";

        Employee employee = TestDataUtils.createFullyPopulatedEmployee(id);
        EmployeeDto employeeDto = TestDataUtils.createFullyPopulatedEmployeeDto(id);

        String name = employee.getFirstName();

        when(employeeRepository.findByFirstNameContainingOrLastNameContaining(name, name))
                .thenReturn(List.of(employee));
        when(employeeMapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        List<EmployeeDto> result = employeeService.searchEmployeeByName(name);

        verify(employeeRepository).findByFirstNameContainingOrLastNameContaining(name, name);
        verify(employeeMapper).employeeToEmployeeDto(employee);

        assertEquals(List.of(employeeDto), result);
    }
}
