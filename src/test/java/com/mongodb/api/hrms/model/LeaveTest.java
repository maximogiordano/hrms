package com.mongodb.api.hrms.model;

import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaveTest {
    @ParameterizedTest
    @MethodSource("testData")
    void test(LocalDate startDate, LocalDate endDate, int numberOfDays) {
        String id = "68294342f43b2332e73fbb07";
        Leave leave = TestDataUtils.createFullyPopulatedLeave(id);

        leave.setStartDate(startDate);
        leave.setEndDate(endDate);

        assertEquals(numberOfDays, leave.getNumberOfDays());
    }

    static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 1), 1),
                Arguments.of(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 2), 2),
                Arguments.of(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 31), 31),
                Arguments.of(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 2, 1), 32),
                Arguments.of(LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), 367)
        );
    }
}
