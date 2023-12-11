package com.udacity.jdnd.course3.critterchronologer.service;

import com.udacity.jdnd.course3.critterchronologer.entities.Customer;
import com.udacity.jdnd.course3.critterchronologer.entities.Pet;
import com.udacity.jdnd.course3.critterchronologer.repository.CustomerRepository;
import com.udacity.jdnd.course3.critterchronologer.repository.PetRepository;
import com.udacity.jdnd.course3.critterchronologer.dto.response.CustomerResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    public CustomerResponseDTO createCustomer(CustomerResponseDTO customerResponseDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerResponseDTO, customer);

        if (CollectionUtils.isEmpty(customerResponseDTO.getPetIds())) {
            customer.setPets(new ArrayList<>());
        } else {
            List<Pet> petList = petRepository.findByIdIn(customerResponseDTO.getPetIds());
            customer.getPets().addAll(petList);
        }

        Customer createdCustomer = customerRepository.save(customer);
        return toCustomerDTO(createdCustomer);
    }
    public List<CustomerResponseDTO> findAll() {
        List<Customer> customerList = customerRepository.findAll();
        return toCustomerDTOsList(customerList);
    }

    public CustomerResponseDTO getPetOwner(Long petId) {
        Pet pet = petRepository.getOne(petId);
        return toCustomerDTO(pet.getCustomer());
    }

    private CustomerResponseDTO toCustomerDTO(Customer customer){
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        BeanUtils.copyProperties(customer, customerResponseDTO);
        customerResponseDTO.setPetIds(customer.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        return customerResponseDTO;
    }

    private List<CustomerResponseDTO> toCustomerDTOsList(List<Customer> customerList) {
        if (CollectionUtils.isEmpty(customerList)) {
            return new ArrayList<>();
        }

        return customerList.stream().map(this::toCustomerDTO).collect(Collectors.toList());
    }
}
