package com.udacity.jdnd.course3.critterchronologer.entities;

import com.udacity.jdnd.course3.critterchronologer.enums.EmployeeSkill;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Setter
@Getter
public class Employee extends Humanoid {

    @ElementCollection
    private Set<EmployeeSkill> skills;

    @ElementCollection
    private Set<DayOfWeek> daysAvailable;
}
