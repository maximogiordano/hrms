package com.mongodb.api.hrms.mapper;

import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee employeeDtoToEmployee(EmployeeDto employeeDto);

    EmployeeDto employeeToEmployeeDto(Employee employee);
}
