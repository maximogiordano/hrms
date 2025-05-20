package com.mongodb.api.hrms.model;

import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("testData")
    void test(String testName, Employee employee, int expected) {
        assertEquals(expected, employee.getBalance("annual"));
    }

    static Stream<Arguments> testData() {
        String id = "682904c2ad128f5295905416";

        Employee employeeWithNullLeaveBalances = TestDataUtils.createFullyPopulatedEmployee(id);
        employeeWithNullLeaveBalances.setLeaveBalances(null);

        Employee employeeWithoutAnnualBalance = TestDataUtils.createFullyPopulatedEmployee(id);
        employeeWithoutAnnualBalance.getLeaveBalances().remove("annual");

        Employee employeeWithZeroAnnualBalance = TestDataUtils.createFullyPopulatedEmployee(id);
        employeeWithZeroAnnualBalance.getLeaveBalances().put("annual", 0);

        Employee employeeWithNonZeroAnnualBalance = TestDataUtils.createFullyPopulatedEmployee(id);
        int balance = employeeWithNonZeroAnnualBalance.getLeaveBalances().get("annual");

        return Stream.of(
                Arguments.of("employee without leave balances", employeeWithNullLeaveBalances, 0),
                Arguments.of("employee without balance", employeeWithoutAnnualBalance, 0),
                Arguments.of("employee with zero balance", employeeWithZeroAnnualBalance, 0),
                Arguments.of("employee with non zero balance", employeeWithNonZeroAnnualBalance, balance)
        );
    }
}
