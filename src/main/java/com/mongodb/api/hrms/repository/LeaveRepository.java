package com.mongodb.api.hrms.repository;

import com.mongodb.api.hrms.model.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends MongoRepository<Leave, String> {
}
