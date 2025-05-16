package com.mongodb.api.hrms.repository.custom;

import com.mongodb.api.hrms.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void decrementLeaveBalance(String employeeId, String leaveType, int numberOfDays) {
        Query query = new Query(Criteria.where("_id").is(employeeId));
        Update update = new Update().inc("leaveBalances." + leaveType, -numberOfDays);

        mongoTemplate.updateFirst(query, update, Employee.class);
    }
}
