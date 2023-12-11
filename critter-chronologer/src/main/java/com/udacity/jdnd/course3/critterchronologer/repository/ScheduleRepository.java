package com.udacity.jdnd.course3.critterchronologer.repository;

import com.udacity.jdnd.course3.critterchronologer.entities.Employee;
import com.udacity.jdnd.course3.critterchronologer.entities.Pet;
import com.udacity.jdnd.course3.critterchronologer.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> getAllByPetsContains(Pet pet);

    List<Schedule> getAllByEmployeesContains(Employee employee);

    List<Schedule> getAllByPetsIn(List<Pet> pets);
}
