package com.mongodb.api.hrms.repository;

import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.custom.CustomEmployeeRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String>, CustomEmployeeRepository {
    Optional<Employee> findByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}
