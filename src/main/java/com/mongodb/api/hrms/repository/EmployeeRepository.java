package com.mongodb.api.hrms.repository;

import com.mongodb.api.hrms.model.Employee;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    /**
     * Returns a list of employees that meet the following criteria:
     * <code>firstName like "%" + name + "%" or lastName like "%" + name + "%"</code>
     * (ignores case)
     */
    @Aggregation(pipeline = {"{ $match: { $or: [ " +
            "{ firstName: { $regex: ?0, $options: \"i\" } }, " +
            "{ lastName: { $regex: ?0, $options: \"i\" } } " +
            "] } }"})
    List<Employee> searchEmployeeByName(String name);
}
