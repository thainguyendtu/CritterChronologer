package com.udacity.jdnd.course3.critterchronologer.service;

import com.udacity.jdnd.course3.critterchronologer.entities.Employee;
import com.udacity.jdnd.course3.critterchronologer.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critterchronologer.dto.response.EmployeeResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.dto.request.EmployeeRequestDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeResponseDTO employeeResponseDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeResponseDTO, employee);
        Employee createdEmployee = employeeRepository.save(employee);
        return toEmployeeDTO(createdEmployee);
    }

    @Transactional
    public void setAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    @Transactional
    public EmployeeResponseDTO findById(Long id) {
        Employee employee = employeeRepository.getOne(id);
        return toEmployeeDTO(employee);
    }

    @Transactional
    public List<EmployeeResponseDTO> findEmployees(EmployeeRequestDTO employeeDTO) {
        List<Employee> employees =
                employeeRepository.getAllByDaysAvailableContains(employeeDTO.getDate().getDayOfWeek());
        if (CollectionUtils.isEmpty(employees)) {
            return null;
        }

        employees.removeIf(employee -> !employee.getSkills().containsAll(employeeDTO.getSkills()));

        return employees.stream()
                        .map(this::toEmployeeDTO)
                        .collect(Collectors.toList());
    }

    private EmployeeResponseDTO toEmployeeDTO(Employee employee) {
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();
        BeanUtils.copyProperties(employee, employeeResponseDTO);
        return employeeResponseDTO;
    }
}
