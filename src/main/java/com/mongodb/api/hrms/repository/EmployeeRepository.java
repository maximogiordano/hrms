package com.mongodb.api.hrms.repository;

import com.mongodb.api.hrms.model.Employee;
import com.mongodb.api.hrms.repository.custom.CustomEmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String>, CustomEmployeeRepository {
    Optional<Employee> findByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    @Query("{ $or: [ { firstName: { $regex: ?0, $options: 'i' } }, { lastName: { $regex: ?0, $options: 'i' } } ] }")
    Page<Employee> findByName(String name, Pageable pageable);
}
