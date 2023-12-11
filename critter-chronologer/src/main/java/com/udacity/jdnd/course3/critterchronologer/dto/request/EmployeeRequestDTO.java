package com.udacity.jdnd.course3.critterchronologer.dto.request;

import com.udacity.jdnd.course3.critterchronologer.enums.EmployeeSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a request to find available employees by skills. Does not map
 * to the database directly.
 */

@Setter
@Getter
@NoArgsConstructor
public class EmployeeRequestDTO {

    private Set<EmployeeSkill> skills;
    private LocalDate date;
}
