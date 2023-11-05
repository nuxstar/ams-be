package com.flyerssoft.ams.model.repository;

import com.flyerssoft.ams.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Employee table - Repository interface.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
