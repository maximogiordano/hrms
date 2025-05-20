package com.mongodb.api.hrms.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ObjectMapperTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void jsonToEmployeeDto() throws IOException {
        String json = Files.readString(Path.of("src", "test", "resources", "employee.json"));
        EmployeeDto employeeDto = objectMapper.readValue(json, EmployeeDto.class);
        EmployeeDto expected = TestDataUtils.createFullyPopulatedEmployeeDto(null); // id is read only
        assertEquals(expected, employeeDto);
    }

    @Test
    void employeeDtoToJson() throws IOException {
        EmployeeDto employeeDto = TestDataUtils.createFullyPopulatedEmployeeDto("682904c2ad128f5295905416");
        String json = objectMapper.writeValueAsString(employeeDto);
        String expected = Files.readString(Path.of("src", "test", "resources", "employee.json"));
        assertEquals(objectMapper.readTree(expected), objectMapper.readTree(json));
    }

    @Test
    void jsonToLeaveDto() throws IOException {
        String json = Files.readString(Path.of("src", "test", "resources", "leave.json"));
        LeaveDto employeeDto = objectMapper.readValue(json, LeaveDto.class);
        LeaveDto expected = TestDataUtils.createFullyPopulatedLeaveDto(null); // id is read only
        assertEquals(expected, employeeDto);
    }

    @Test
    void leaveDtoToJson() throws IOException {
        LeaveDto leaveDto = TestDataUtils.createFullyPopulatedLeaveDto("68294342f43b2332e73fbb07");
        String json = objectMapper.writeValueAsString(leaveDto);
        String expected = Files.readString(Path.of("src", "test", "resources", "leave.json"));
        assertEquals(objectMapper.readTree(expected), objectMapper.readTree(json));
    }
}
