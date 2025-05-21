package com.mongodb.api.hrms.controller;

import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.service.EmployeeService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @InjectMocks
    EmployeeController employeeController;

    @Mock
    EmployeeService employeeService;

    @Test
    void createEmployee() {
        EmployeeDto input = TestDataUtils.createFullyPopulatedEmployeeDto(null);
        EmployeeDto output = TestDataUtils.createFullyPopulatedEmployeeDto("682904c2ad128f5295905416");

        when(employeeService.createEmployee(input)).thenReturn(output);

        EmployeeDto result = employeeController.createEmployee(input);

        verify(employeeService).createEmployee(input);
        assertEquals(output, result);
    }

    @Test
    void searchEmployeeByName() {
        EmployeeDto employeeDto = TestDataUtils.createFullyPopulatedEmployeeDto("682904c2ad128f5295905416");

        String name = employeeDto.getFirstName();
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName", "firstName", "phoneNumber"));
        Page<EmployeeDto> employees = new PageImpl<>(List.of(employeeDto), pageable, 1);

        when(employeeService.searchEmployeeByName(name, page, size)).thenReturn(employees);

        Page<EmployeeDto> result = employeeController.searchEmployeeByName(name, page, size);

        verify(employeeService).searchEmployeeByName(name, page, size);
        assertEquals(employees, result);
    }
}
