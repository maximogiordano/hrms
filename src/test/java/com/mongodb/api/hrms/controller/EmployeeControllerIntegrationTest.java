package com.mongodb.api.hrms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.api.hrms.advice.GlobalExceptionHandler;
import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.dto.ErrorDto;
import com.mongodb.api.hrms.dto.ErrorItemDto;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class EmployeeControllerIntegrationTest extends IntegrationTest {
    @MockitoBean
    EmployeeRepository employeeRepository;

    @Test
    void createExistingEmployee() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

        String id = "682904c2ad128f5295905416";

        EmployeeDto input = TestDataUtils.createFullyPopulatedEmployeeDto(null);
        Employee foundEmployee = TestDataUtils.createAnotherFullyPopulatedEmployee(id);
        Employee employeeToUpdate = TestDataUtils.createFullyPopulatedEmployee(id);
        Employee savedEmployee = TestDataUtils.createFullyPopulatedEmployee(id);
        EmployeeDto savedEmployeeDto = TestDataUtils.createFullyPopulatedEmployeeDto(id);

        when(employeeRepository.findByFirstNameAndLastNameAndPhoneNumber(input.getFirstName(), input.getLastName(),
                input.getPhoneNumber())).thenReturn(Optional.of(foundEmployee));
        when(employeeRepository.save(employeeToUpdate)).thenReturn(savedEmployee);

        MockHttpServletResponse response = mockMvc.perform(post("/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        JsonNode expected = objectMapper.readTree(objectMapper.writeValueAsString(savedEmployeeDto));
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }

    @Test
    void createNewEmployee() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

        String id = "682904c2ad128f5295905416";

        EmployeeDto input = TestDataUtils.createFullyPopulatedEmployeeDto(null);
        Employee inputAsEntity = TestDataUtils.createFullyPopulatedEmployee(null);
        Employee savedEmployee = TestDataUtils.createFullyPopulatedEmployee(id);
        EmployeeDto savedEmployeeDto = TestDataUtils.createFullyPopulatedEmployeeDto(id);

        when(employeeRepository.findByFirstNameAndLastNameAndPhoneNumber(input.getFirstName(), input.getLastName(),
                input.getPhoneNumber())).thenReturn(Optional.empty());
        when(employeeRepository.save(inputAsEntity)).thenReturn(savedEmployee);

        MockHttpServletResponse response = mockMvc.perform(post("/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        JsonNode expected = objectMapper.readTree(objectMapper.writeValueAsString(savedEmployeeDto));
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }

    @Test
    void createEmployeeWithAccessDenied() throws Exception {
        String token = performSuccessfulLogin("jane.smith", "j4n3.sm1th", "USER");

        EmployeeDto input = TestDataUtils.createFullyPopulatedEmployeeDto(null);

        int code = GlobalExceptionHandler.AUTHORIZATION_DENIED_ERROR_CODE;
        String detail = "Access Denied";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, detail);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto errorDto = new ErrorDto(errors);

        MockHttpServletResponse response = mockMvc.perform(post("/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(403, response.getStatus());

        JsonNode expected = objectMapper.readTree(objectMapper.writeValueAsString(errorDto));
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }

    @Test
    void searchEmployeeByName() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

        String id = "682904c2ad128f5295905416";
        Employee employee = TestDataUtils.createFullyPopulatedEmployee(id);
        EmployeeDto employeeDto = TestDataUtils.createFullyPopulatedEmployeeDto(id);

        when(employeeRepository.findByFirstNameContainingOrLastNameContaining(employee.getFirstName(),
                employee.getFirstName())).thenReturn(List.of(employee));

        String response = mockMvc.perform(get("/employees?name=" + employee.getFirstName())
                        .header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode expected = objectMapper.readTree(objectMapper.writeValueAsString(List.of(employeeDto)));
        JsonNode actual = objectMapper.readTree(response);

        assertEquals(expected, actual);
    }
}
