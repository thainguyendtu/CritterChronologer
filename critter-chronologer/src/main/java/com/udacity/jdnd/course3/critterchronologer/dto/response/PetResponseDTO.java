package com.udacity.jdnd.course3.critterchronologer.dto.response;

import com.udacity.jdnd.course3.critterchronologer.enums.PetType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents the form that pet request and response data takes. Does not map
 * to the database directly.
 */
@Setter
@Getter
public class PetResponseDTO {

    private Long id;
    private PetType type;
    private String name;
    private Long ownerId;
    private LocalDate birthDate;
    private String notes;
}
