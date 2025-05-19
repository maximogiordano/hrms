package com.mongodb.api.hrms.validation;

import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaveDatesValidatorTest {
    LeaveDatesValidator leaveDatesValidator = new LeaveDatesValidator(); // system under test

    @ParameterizedTest
    @MethodSource("testData")
    void test(LeaveDto value, boolean expected) {
        assertEquals(expected, leaveDatesValidator.isValid(value, null));
    }

    static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(TestDataUtils.createLeaveDto(null, null), true),
                Arguments.of(TestDataUtils.createLeaveDto(null, LocalDate.of(2000, 1, 1)), true),
                Arguments.of(TestDataUtils.createLeaveDto(LocalDate.of(2000, 1, 1), null), true),
                Arguments.of(TestDataUtils.createLeaveDto(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 1)), true),
                Arguments.of(TestDataUtils.createLeaveDto(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 2)), true),
                Arguments.of(TestDataUtils.createLeaveDto(LocalDate.of(2000, 1, 1), LocalDate.of(1999, 12, 31)), false)
        );
    }
}
