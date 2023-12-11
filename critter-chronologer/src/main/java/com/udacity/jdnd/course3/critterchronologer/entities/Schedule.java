package com.udacity.jdnd.course3.critterchronologer.entities;

import com.udacity.jdnd.course3.critterchronologer.enums.EmployeeSkill;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ElementCollection
    private Set<EmployeeSkill> skills;

    @ManyToMany
    @JoinTable(
            name = "schedule_employee",
            joinColumns = {
                    @JoinColumn(name = "schedule_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "employee_id")
            }
    )
    private List<Employee> employees;

    @ManyToMany
    @JoinTable(
            name = "schedule_pet",
            joinColumns = {
                    @JoinColumn(name = "schedule_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "pet_id")
            }
    )

    private List<Pet> pets;
}
