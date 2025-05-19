package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.AddressDto;
import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.mapper.EmployeeMapper;
import com.mongodb.api.hrms.model.Address;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        EmployeeDto input = createSampleEmployeeDto(null);
        Employee inputAsEntity = createSampleEmployee(null);
        Employee foundEmployee = createSampleEmployee(id);
        Employee employeeToUpdate = createSampleEmployee(id);
        Employee savedEmployee = createSampleEmployee(id);
        EmployeeDto savedEmployeeDto = createSampleEmployeeDto(id);

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

        EmployeeDto input = createSampleEmployeeDto(null);
        Employee inputAsEntity = createSampleEmployee(null);
        Employee savedEmployee = createSampleEmployee(id);
        EmployeeDto savedEmployeeDto = createSampleEmployeeDto(id);

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

        Employee employee = createSampleEmployee(id);
        EmployeeDto employeeDto = createSampleEmployeeDto(id);

        String name = employee.getFirstName();

        when(employeeRepository.findByFirstNameContainingOrLastNameContaining(name, name))
                .thenReturn(List.of(employee));
        when(employeeMapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        List<EmployeeDto> result = employeeService.searchEmployeeByName(name);

        verify(employeeRepository).findByFirstNameContainingOrLastNameContaining(name, name);
        verify(employeeMapper).employeeToEmployeeDto(employee);

        assertEquals(List.of(employeeDto), result);
    }

    EmployeeDto createSampleEmployeeDto(String id) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(id);
        employeeDto.setFirstName("Ulysses");
        employeeDto.setLastName("Morris");
        employeeDto.setEmail("ulysses.morris@mail.com");
        employeeDto.setPhoneNumber("40-792-3967");
        employeeDto.setHireDate(LocalDate.of(2019, 10, 12));
        employeeDto.setJobId("DIR");
        employeeDto.setSalary(BigDecimal.valueOf(537755));
        employeeDto.setManagerId("682904c2ad128f5295905415");

        AddressDto address = new AddressDto();

        address.setStreet("7184 Riverview Court");
        address.setCity("Springfield");
        address.setState("MI");
        address.setZipCode("22708");

        employeeDto.setAddress(address);

        Map<String, Integer> leaveBalances = new HashMap<>();

        leaveBalances.put("annual", 27);
        leaveBalances.put("sick", 1);
        leaveBalances.put("maternity", 7);
        leaveBalances.put("paternity", 4);
        leaveBalances.put("bereavement", 18);
        leaveBalances.put("unpaid", 25);
        leaveBalances.put("compensatory", 9);
        leaveBalances.put("study", 17);
        leaveBalances.put("casual", 16);
        leaveBalances.put("sabbatical", 14);

        employeeDto.setLeaveBalances(leaveBalances);

        return employeeDto;
    }

    Employee createSampleEmployee(String id) {
        Employee employee = new Employee();

        employee.setId(id);
        employee.setFirstName("Ulysses");
        employee.setLastName("Morris");
        employee.setEmail("ulysses.morris@mail.com");
        employee.setPhoneNumber("40-792-3967");
        employee.setHireDate(LocalDate.of(2019, 10, 12));
        employee.setJobId("DIR");
        employee.setSalary(BigDecimal.valueOf(537755));
        employee.setManagerId("682904c2ad128f5295905415");

        Address address = new Address();

        address.setStreet("7184 Riverview Court");
        address.setCity("Springfield");
        address.setState("MI");
        address.setZipCode("22708");

        employee.setAddress(address);

        Map<String, Integer> leaveBalances = new HashMap<>();

        leaveBalances.put("annual", 27);
        leaveBalances.put("sick", 1);
        leaveBalances.put("maternity", 7);
        leaveBalances.put("paternity", 4);
        leaveBalances.put("bereavement", 18);
        leaveBalances.put("unpaid", 25);
        leaveBalances.put("compensatory", 9);
        leaveBalances.put("study", 17);
        leaveBalances.put("casual", 16);
        leaveBalances.put("sabbatical", 14);

        employee.setLeaveBalances(leaveBalances);

        return employee;
    }
}
