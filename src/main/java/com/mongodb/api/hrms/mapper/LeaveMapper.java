package com.mongodb.api.hrms.mapper;

import com.mongodb.api.hrms.dto.LeaveDto;
import com.mongodb.api.hrms.model.Leave;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public interface LeaveMapper {
    Leave leaveDtoToLeave(LeaveDto leaveDto);

    LeaveDto leaveToLeaveDto(Leave leave);
}
