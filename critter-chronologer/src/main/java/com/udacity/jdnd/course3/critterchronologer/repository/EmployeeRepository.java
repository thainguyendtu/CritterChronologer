package com.udacity.jdnd.course3.critterchronologer.repository;

import com.udacity.jdnd.course3.critterchronologer.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> getAllByDaysAvailableContains(DayOfWeek dayOfWeek);
}
