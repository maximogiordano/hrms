package com.mongodb.api.hrms.controller;

import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.service.LeaveService;
import com.mongodb.api.hrms.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveControllerTest {
    @InjectMocks
    LeaveController leaveController;

    @Mock
    LeaveService leaveService;

    @Test
    void createLeave() {
        LeaveDto input = TestDataUtils.createFullyPopulatedLeaveDto(null);
        LeaveDto output = TestDataUtils.createFullyPopulatedLeaveDto("68294342f43b2332e73fbb07");

        when(leaveService.createLeave(input)).thenReturn(output);

        LeaveDto result = leaveController.createLeave(input);

        verify(leaveService).createLeave(input);
        assertEquals(output, result);
    }
}
