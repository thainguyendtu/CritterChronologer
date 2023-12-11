package com.udacity.jdnd.course3.critterchronologer.service;

import com.udacity.jdnd.course3.critterchronologer.entities.Customer;
import com.udacity.jdnd.course3.critterchronologer.entities.Employee;
import com.udacity.jdnd.course3.critterchronologer.entities.Pet;
import com.udacity.jdnd.course3.critterchronologer.entities.Schedule;
import com.udacity.jdnd.course3.critterchronologer.repository.CustomerRepository;
import com.udacity.jdnd.course3.critterchronologer.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critterchronologer.repository.PetRepository;
import com.udacity.jdnd.course3.critterchronologer.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critterchronologer.dto.response.ScheduleResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    public List<ScheduleResponseDTO> findAll() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        return toScheduleDTOsList(scheduleList);
    }

    @Transactional
    public ScheduleResponseDTO createSchedule(ScheduleResponseDTO scheduleResponseDTO) {
        Schedule schedule = new Schedule();
        schedule.setDate(scheduleResponseDTO.getDate());
        schedule.setSkills(scheduleResponseDTO.getSkills());

        if (scheduleResponseDTO.getEmployeeIds() != null && !scheduleResponseDTO.getEmployeeIds().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(scheduleResponseDTO.getEmployeeIds());
            schedule.setEmployees(employees);
        }

        if (scheduleResponseDTO.getPetIds() != null && !scheduleResponseDTO.getPetIds().isEmpty()) {
            List<Pet> pets = petRepository.findAllById(scheduleResponseDTO.getPetIds());
            schedule.setPets(pets);
        }

        Schedule createdSchedule = scheduleRepository.save(schedule);
        scheduleResponseDTO.setId(createdSchedule.getId());

        return scheduleResponseDTO;
    }

    @Transactional
    public List<ScheduleResponseDTO> getAllSchedulesForPet(Long petId) {
        Pet pet = petRepository.getOne(petId);
        List<Schedule> scheduleList = scheduleRepository.getAllByPetsContains(pet);
        return toScheduleDTOsList(scheduleList);
    }

    @Transactional
    public List<ScheduleResponseDTO> getAllSchedulesForEmployee(Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> scheduleList = scheduleRepository.getAllByEmployeesContains(employee);

        return toScheduleDTOsList(scheduleList);
    }

    @Transactional
    public List<ScheduleResponseDTO> getAllScheduleForCustomer(Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Schedule> scheduleList = scheduleRepository.getAllByPetsIn(customer.getPets());

        return toScheduleDTOsList(scheduleList);
    }

    private List<ScheduleResponseDTO> toScheduleDTOsList(List<Schedule> schedulesList) {
        if (CollectionUtils.isEmpty(schedulesList)) {
            return new ArrayList<>();
        }

        return schedulesList.stream()
                            .map(this::toScheduleDTO)
                            .collect(Collectors.toList());
    }

    private ScheduleResponseDTO toScheduleDTO(Schedule schedule) {
        ScheduleResponseDTO scheduleResponseDTO = new ScheduleResponseDTO();
        BeanUtils.copyProperties(schedule, scheduleResponseDTO);
        List<Long> employeeIds = schedule.getEmployees()
                                         .stream()
                                         .map(Employee::getId)
                                         .collect(Collectors.toList());

        List<Long> petIds = schedule.getPets()
                                    .stream()
                                    .map(Pet::getId)
                                    .collect(Collectors.toList());

        scheduleResponseDTO.setEmployeeIds(employeeIds);
        scheduleResponseDTO.setPetIds(petIds);

        return scheduleResponseDTO;
    }
}
