package com.udacity.jdnd.course3.critterchronologer.service;

import com.udacity.jdnd.course3.critterchronologer.entities.Customer;
import com.udacity.jdnd.course3.critterchronologer.entities.Pet;
import com.udacity.jdnd.course3.critterchronologer.dto.response.PetResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.repository.CustomerRepository;
import com.udacity.jdnd.course3.critterchronologer.repository.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    public PetResponseDTO createPet(PetResponseDTO petResponseDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petResponseDTO, pet);
        Long ownerId = petResponseDTO.getOwnerId();
        Customer customer = customerRepository.getOne(ownerId);
        pet.setCustomer(customer);
        if (customer.getPets() == null) {
            customer.setPets(new ArrayList<>());
        }

        Pet petCreated = petRepository.save(pet);
        customer.getPets().add(petCreated);
        customerRepository.save(customer);

        return toPetDTO(pet);
    }

    @Transactional
    public PetResponseDTO findById(Long ids) {
        Pet pet = petRepository.getOne(ids);
        return toPetDTO(pet);
    }

    @Transactional
    public List<PetResponseDTO> getPetsByOwner(Long ownerId) {
        List<Pet> petsList = petRepository.getAllByCustomerId(ownerId);
        return toPetsListDTO(petsList);
    }

    @Transactional
    public List<PetResponseDTO> getAllPets() {
        List<Pet> petsList = petRepository.findAll();
        return toPetsListDTO(petsList);
    }

    private List<PetResponseDTO> toPetsListDTO(List<Pet> petsList) {
        if (CollectionUtils.isEmpty(petsList)) {
            return new ArrayList<>();
        }

        return petsList.stream()
                       .map(this::toPetDTO)
                       .collect(Collectors.toList());
    }

    private PetResponseDTO toPetDTO(Pet pet) {
        PetResponseDTO petResponseDTO = new PetResponseDTO();
        BeanUtils.copyProperties(pet, petResponseDTO);
        petResponseDTO.setOwnerId(pet.getCustomer().getId());
        return petResponseDTO;
    }
}
