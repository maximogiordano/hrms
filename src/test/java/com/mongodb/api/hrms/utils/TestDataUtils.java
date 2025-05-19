package com.mongodb.api.hrms.utils;

import com.mongodb.api.hrms.dto.AddressDto;
import com.mongodb.api.hrms.dto.EmployeeDto;
import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.model.Address;
import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.model.Leave;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestDataUtils {
    /**
     * @return a new instance of a fully populated employee with a fixed set of data.
     */
    public static Employee createFullyPopulatedEmployee(String id) {
        Employee employee = new Employee();

        employee.setId(id);
        employee.setFirstName("Ulysses");
        employee.setLastName("Morris");
        employee.setEmail("ulysses.morris@mail.com");
        employee.setPhoneNumber("940-792-3967");
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

    /**
     * @return the same as {@link #createFullyPopulatedEmployee(String)}, but as a DTO.
     */
    public static EmployeeDto createFullyPopulatedEmployeeDto(String id) {
        EmployeeDto employee = new EmployeeDto();

        employee.setId(id);
        employee.setFirstName("Ulysses");
        employee.setLastName("Morris");
        employee.setEmail("ulysses.morris@mail.com");
        employee.setPhoneNumber("940-792-3967");
        employee.setHireDate(LocalDate.of(2019, 10, 12));
        employee.setJobId("DIR");
        employee.setSalary(BigDecimal.valueOf(537755));
        employee.setManagerId("682904c2ad128f5295905415");

        AddressDto address = new AddressDto();

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

    /**
     * @return a new instance of a fully populated employee with a fixed set of data, distinct from
     * {@link #createFullyPopulatedEmployee(String)}
     */
    public static Employee createAnotherFullyPopulatedEmployee(String id) {
        Employee employee = new Employee();

        employee.setId(id);
        employee.setFirstName("Julia");
        employee.setLastName("Turner");
        employee.setEmail("julia.turner@gmail.com");
        employee.setPhoneNumber("579-231-7389");
        employee.setHireDate(LocalDate.of(2024, 9, 23));
        employee.setJobId("PM");
        employee.setSalary(BigDecimal.valueOf(259701));
        employee.setManagerId("682904c2ad128f5295905416");

        Address address = new Address();

        address.setStreet("5304 Bayview Avenue");
        address.setCity("Clayton");
        address.setState("FL");
        address.setZipCode("90501");

        employee.setAddress(address);

        Map<String, Integer> leaveBalances = new HashMap<>();

        leaveBalances.put("annual", 13);
        leaveBalances.put("sick", 13);
        leaveBalances.put("maternity", 11);
        leaveBalances.put("paternity", 27);
        leaveBalances.put("bereavement", 26);
        leaveBalances.put("unpaid", 20);
        leaveBalances.put("compensatory", 3);
        leaveBalances.put("study", 26);
        leaveBalances.put("casual", 19);
        leaveBalances.put("sabbatical", 22);

        employee.setLeaveBalances(leaveBalances);

        return employee;
    }

    /**
     * @return a new instance of a fully populated leave with a fixed set of data.
     */
    public static Leave createFullyPopulatedLeave(String id) {
        Leave leave = new Leave();

        leave.setId(id);
        leave.setEmployeeId("682904c2ad128f5295905416");
        leave.setLeaveType("annual");
        leave.setStartDate(LocalDate.of(2025, 5, 19));
        leave.setEndDate(LocalDate.of(2025, 5, 23));
        leave.setStatus("pending");
        leave.setApprovedBy("682904c2ad128f5295905415");

        return leave;
    }

    /**
     * @return the same as {@link #createFullyPopulatedLeave(String)}, but as a DTO.
     */
    public static LeaveDto createFullyPopulatedLeaveDto(String id) {
        LeaveDto leaveDto = new LeaveDto();

        leaveDto.setId(id);
        leaveDto.setEmployeeId("682904c2ad128f5295905416");
        leaveDto.setLeaveType("annual");
        leaveDto.setStartDate(LocalDate.of(2025, 5, 19));
        leaveDto.setEndDate(LocalDate.of(2025, 5, 23));
        leaveDto.setStatus("pending");
        leaveDto.setApprovedBy("682904c2ad128f5295905415");

        return leaveDto;
    }
}
