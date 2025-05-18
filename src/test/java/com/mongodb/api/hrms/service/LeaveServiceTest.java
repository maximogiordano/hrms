package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.exception.DocumentNotFoundException;
import com.mongodb.api.hrms.exception.DocumentOperationException;
import com.mongodb.api.hrms.mapper.LeaveMapper;
import com.mongodb.api.hrms.model.Address;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.model.Leave;
import com.mongodb.api.hrms.repository.EmployeeRepository;
import com.mongodb.api.hrms.repository.LeaveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {
    @InjectMocks
    LeaveService leaveService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    LeaveRepository leaveRepository;

    @Mock
    LeaveMapper leaveMapper;

    @Test
    void createLeaveSuccessfully() {
        LeaveDto input = getInput();
        Leave inputAsEntity = getInputAsEntity();
        Leave outputAsEntity = getOutputAsEntity();
        LeaveDto output = getOutput();
        Employee employee = getEmployee();

        when(employeeRepository.findById("682904c2ad128f5295905416")).thenReturn(Optional.of(employee));
        when(leaveMapper.leaveDtoToLeave(input)).thenReturn(inputAsEntity);
        when(leaveRepository.save(inputAsEntity)).thenReturn(outputAsEntity);
        when(leaveMapper.leaveToLeaveDto(outputAsEntity)).thenReturn(output);

        LeaveDto result = leaveService.createLeave(input);

        verify(employeeRepository).findById("682904c2ad128f5295905416");
        verify(leaveMapper).leaveDtoToLeave(input);
        verify(leaveRepository).save(inputAsEntity);
        verify(employeeRepository).decrementLeaveBalance("682904c2ad128f5295905416", "annual", 5);
        verify(leaveMapper).leaveToLeaveDto(outputAsEntity);

        assertEquals(output, result);
    }

    @Test
    void createLeaveWithEmployeeNotFound() {
        LeaveDto input = getInput();

        when(employeeRepository.findById("682904c2ad128f5295905416")).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> leaveService.createLeave(input), "employee not found");

        verify(employeeRepository).findById("682904c2ad128f5295905416");
    }

    @Test
    void createLeaveWithInvalidBalance() {
        LeaveDto input = getInput();
        input.setEndDate(LocalDate.of(2025, 6, 19));

        Leave inputAsEntity = getInputAsEntity();
        inputAsEntity.setEndDate(LocalDate.of(2025, 6, 19));

        Employee employee = getEmployee();

        when(employeeRepository.findById("682904c2ad128f5295905416")).thenReturn(Optional.of(employee));
        when(leaveMapper.leaveDtoToLeave(input)).thenReturn(inputAsEntity);

        assertThrows(DocumentOperationException.class, () -> leaveService.createLeave(input), "insufficient balance");

        verify(employeeRepository).findById("682904c2ad128f5295905416");
        verify(leaveMapper).leaveDtoToLeave(input);
    }

    @Test
    void createLeaveWithInvalidApprovedBy() {
        LeaveDto input = getInput();
        input.setApprovedBy("682904c2ad128f5295905416");

        Leave inputAsEntity = getInputAsEntity();
        inputAsEntity.setApprovedBy("682904c2ad128f5295905416");

        Employee employee = getEmployee();

        when(employeeRepository.findById("682904c2ad128f5295905416")).thenReturn(Optional.of(employee));
        when(leaveMapper.leaveDtoToLeave(input)).thenReturn(inputAsEntity);

        assertThrows(DocumentOperationException.class, () -> leaveService.createLeave(input),
                "approved_by must be employee.manager_id");

        verify(employeeRepository).findById("682904c2ad128f5295905416");
        verify(leaveMapper).leaveDtoToLeave(input);
    }

    private static LeaveDto getInput() {
        LeaveDto leaveDto = new LeaveDto();

        leaveDto.setId(null);
        leaveDto.setEmployeeId("682904c2ad128f5295905416");
        leaveDto.setLeaveType("annual");
        leaveDto.setStartDate(LocalDate.of(2025, 5, 19));
        leaveDto.setEndDate(LocalDate.of(2025, 5, 23));
        leaveDto.setStatus("pending");
        leaveDto.setApprovedBy("682904c2ad128f5295905415");

        return leaveDto;
    }

    private static Leave getInputAsEntity() {
        Leave leave = new Leave();

        leave.setId(null);
        leave.setEmployeeId("682904c2ad128f5295905416");
        leave.setLeaveType("annual");
        leave.setStartDate(LocalDate.of(2025, 5, 19));
        leave.setEndDate(LocalDate.of(2025, 5, 23));
        leave.setStatus("pending");
        leave.setApprovedBy("682904c2ad128f5295905415");

        return leave;
    }

    private static Leave getOutputAsEntity() {
        Leave leave = new Leave();

        leave.setId("68294342f43b2332e73fbb07");
        leave.setEmployeeId("682904c2ad128f5295905416");
        leave.setLeaveType("annual");
        leave.setStartDate(LocalDate.of(2025, 5, 19));
        leave.setEndDate(LocalDate.of(2025, 5, 23));
        leave.setStatus("pending");
        leave.setApprovedBy("682904c2ad128f5295905415");

        return leave;
    }

    private static LeaveDto getOutput() {
        LeaveDto leaveDto = new LeaveDto();

        leaveDto.setId("68294342f43b2332e73fbb07");
        leaveDto.setEmployeeId("682904c2ad128f5295905416");
        leaveDto.setLeaveType("annual");
        leaveDto.setStartDate(LocalDate.of(2025, 5, 19));
        leaveDto.setEndDate(LocalDate.of(2025, 5, 23));
        leaveDto.setStatus("pending");
        leaveDto.setApprovedBy("682904c2ad128f5295905415");

        return leaveDto;
    }

    private static Employee getEmployee() {
        Employee employee = new Employee();

        employee.setId("682904c2ad128f5295905416");
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

        Map<String, Integer> balances = new HashMap<>();

        balances.put("annual", 27);
        balances.put("sick", 1);
        balances.put("maternity", 7);
        balances.put("paternity", 4);
        balances.put("bereavement", 18);
        balances.put("unpaid", 25);
        balances.put("compensatory", 9);
        balances.put("study", 17);
        balances.put("casual", 16);
        balances.put("sabbatical", 14);

        employee.setLeaveBalances(balances);

        return employee;
    }
}
