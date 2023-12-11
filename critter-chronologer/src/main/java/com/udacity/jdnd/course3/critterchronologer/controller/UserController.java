package com.udacity.jdnd.course3.critterchronologer.controller;

import com.udacity.jdnd.course3.critterchronologer.dto.request.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critterchronologer.dto.response.CustomerResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.dto.response.EmployeeResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.service.CustomerService;
import com.udacity.jdnd.course3.critterchronologer.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerResponseDTO saveCustomer(@RequestBody CustomerResponseDTO customerResponseDTO) {
        return customerService.createCustomer(customerResponseDTO);
    }

    @GetMapping("/customer")
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerResponseDTO getPetOwner(@PathVariable Long petId) {
        return customerService.getPetOwner(petId);
    }

    @PostMapping("/employee")
    public EmployeeResponseDTO saveEmployee(@RequestBody EmployeeResponseDTO employeeResponseDTO) {
        return employeeService.createEmployee(employeeResponseDTO);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeResponseDTO getEmployee(@PathVariable Long employeeId) {
        return employeeService.findById(employeeId);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@PathVariable Long employeeId, @RequestBody Set<DayOfWeek> daysAvailable) {
        employeeService.setAvailability(employeeId, daysAvailable);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeResponseDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        return employeeService.findEmployees(employeeDTO);
    }
}
