package com.mongodb.api.hrms.controller;

import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeDto createEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @GetMapping
    public List<EmployeeDto> searchEmployeeByName(@RequestParam("name") String name) {
        return employeeService.searchEmployeeByName(name);
    }

    @PostMapping("/{id}/leaves")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLeave(@PathVariable("id") String id, @RequestBody LeaveDto leaveDto) {
        employeeService.createLeave(id, leaveDto);
    }
}
