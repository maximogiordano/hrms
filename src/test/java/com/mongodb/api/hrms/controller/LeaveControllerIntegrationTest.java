package com.mongodb.api.hrms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.api.hrms.advice.GlobalExceptionHandler;
import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.model.Leave;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.repository.LeaveRepository;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class LeaveControllerIntegrationTest extends IntegrationTest {
    @MockitoBean
    EmployeeRepository employeeRepository;

    @MockitoBean
    LeaveRepository leaveRepository;

    @Test
    void createLeaveSuccessfullyAsAdmin() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");
        createLeaveSuccessfully(token);
    }

    @Test
    void createLeaveSuccessfullyAsUser() throws Exception {
        String token = performSuccessfulLogin("jane.smith", "j4n3.sm1th", "USER");
        createLeaveSuccessfully(token);
    }

    void createLeaveSuccessfully(String token) throws Exception {
        String id = "68294342f43b2332e73fbb07";

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        Leave inputAsEntity = TestDataUtils.createFullyPopulatedLeave(null);
        Leave outputAsEntity = TestDataUtils.createFullyPopulatedLeave(id);
        LeaveDto output = TestDataUtils.createFullyPopulatedLeaveDto(id);
        Employee employee = TestDataUtils.createFullyPopulatedEmployee(input.getEmployeeId());

        when(employeeRepository.findById(input.getEmployeeId())).thenReturn(Optional.of(employee));
        when(leaveRepository.save(inputAsEntity)).thenReturn(outputAsEntity);

        MockHttpServletResponse response = mockMvc.perform(post("/leaves")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        JsonNode expected = objectMapper.readTree(objectMapper.writeValueAsString(output));
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }

    @Test
    void createLeaveWithInvalidDates() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        input.setEndDate(LocalDate.of(2025, 5, 18)); // endDate < startDate

        String output = "{\"errors\":[{\"code\":" + GlobalExceptionHandler.CONSTRAINT_VIOLATION_ERROR_CODE +
                ",\"detail\":\"start_date must not be after end_date\"}]}";

        MockHttpServletResponse response = mockMvc.perform(post("/leaves")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        JsonNode expected = objectMapper.readTree(output);
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }

    @Test
    void createLeaveWithEmployeeNotFound() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        String output = "{\"errors\":[{\"code\":" + GlobalExceptionHandler.DOCUMENT_NOT_FOUND_ERROR_CODE +
                ",\"detail\":\"employee not found\"}]}";

        when(employeeRepository.findById(input.getEmployeeId())).thenReturn(Optional.empty());

        MockHttpServletResponse response = mockMvc.perform(post("/leaves")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

        JsonNode expected = objectMapper.readTree(output);
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }

    @Test
    void createLeaveWithInsufficientBalance() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);

        String output = "{\"errors\":[{\"code\":" + GlobalExceptionHandler.DOCUMENT_OPERATION_ERROR_CODE +
                ",\"detail\":\"insufficient balance\"}]}";

        Employee employee = TestDataUtils.createFullyPopulatedEmployee(input.getEmployeeId());
        employee.getLeaveBalances().put("annual", 4); // input contains a leave request of 5 days

        when(employeeRepository.findById(input.getEmployeeId())).thenReturn(Optional.of(employee));

        MockHttpServletResponse response = mockMvc.perform(post("/leaves")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());

        JsonNode expected = objectMapper.readTree(output);
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }

    @Test
    void createLeaveWithInvalidApprovedBy() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        input.setApprovedBy("682904c2ad128f5295905416"); // set an invalid manager

        String output = "{\"errors\":[{\"code\":" + GlobalExceptionHandler.DOCUMENT_OPERATION_ERROR_CODE +
                ",\"detail\":\"approved_by must be employee.manager_id\"}]}";

        Employee employee = TestDataUtils.createFullyPopulatedEmployee(input.getEmployeeId());

        when(employeeRepository.findById(input.getEmployeeId())).thenReturn(Optional.of(employee));

        MockHttpServletResponse response = mockMvc.perform(post("/leaves")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());

        JsonNode expected = objectMapper.readTree(output);
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }
}
