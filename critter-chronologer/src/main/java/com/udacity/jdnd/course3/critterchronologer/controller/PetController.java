package com.udacity.jdnd.course3.critterchronologer.controller;

import com.udacity.jdnd.course3.critterchronologer.dto.response.PetResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @PostMapping
    public PetResponseDTO savePet(@RequestBody PetResponseDTO petResponseDTO) {
        return petService.createPet(petResponseDTO);
    }

    @GetMapping("/{petId}")
    public PetResponseDTO getPet(@PathVariable Long petId) {
        return petService.findById(petId);
    }

    @GetMapping()
    public List<PetResponseDTO> getPets() {
        return petService.getAllPets();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetResponseDTO> getPetsByOwner(@PathVariable Long ownerId) {
        return petService.getPetsByOwner(ownerId);
    }
}
