package com.mongodb.api.hrms.repository.custom;

import com.mongodb.api.hrms.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomEmployeeRepositoryImplTest {
    @InjectMocks
    CustomEmployeeRepositoryImpl customEmployeeRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @Test
    void decrementLeaveBalance() {
        String employeeId = "68294342f43b2332e73fbb07";
        String leaveType = "annual";
        int numberOfDays = 5;

        var query = new Query(Criteria.where("_id").is(employeeId));
        var update = new Update().inc("leaveBalances." + leaveType, -numberOfDays);

        customEmployeeRepository.decrementLeaveBalance(employeeId, leaveType, numberOfDays);

        verify(mongoTemplate).updateFirst(query, update, Employee.class);
    }
}
