package com.mongodb.api.hrms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.model.Leave;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.repository.LeaveRepository;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
    void createLeave() throws Exception {
        String token = performSuccessfulLogin("john.doe", "j0hn.d0e", "ADMIN");

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

        assertEquals(201, response.getStatus());

        JsonNode expected = objectMapper.readTree(objectMapper.writeValueAsString(output));
        JsonNode actual = objectMapper.readTree(response.getContentAsString());

        assertEquals(expected, actual);
    }
}
