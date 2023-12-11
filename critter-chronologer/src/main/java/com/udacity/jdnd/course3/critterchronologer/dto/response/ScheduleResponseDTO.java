package com.udacity.jdnd.course3.critterchronologer.dto.response;


import com.udacity.jdnd.course3.critterchronologer.enums.EmployeeSkill;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents the form that schedule request and response data takes. Does not map
 * to the database directly.
 */

@Setter
@Getter
public class ScheduleResponseDTO {

    private Long id;
    private List<Long> employeeIds;
    private List<Long> petIds;
    private LocalDate date;
    private Set<EmployeeSkill> skills;

    public ScheduleResponseDTO() {
        employeeIds = new ArrayList<>();
        petIds = new ArrayList<>();
    }
}
