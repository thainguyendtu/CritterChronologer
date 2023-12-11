package com.udacity.jdnd.course3.critterchronologer.dto.response;

import com.udacity.jdnd.course3.critterchronologer.enums.EmployeeSkill;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents the form that employee request and response data takes. Does not map
 * to the database directly.
 */

@Setter
@Getter
public class EmployeeResponseDTO {

    private Long id;
    private String name;
    private Set<EmployeeSkill> skills;
    private Set<DayOfWeek> daysAvailable;
}
