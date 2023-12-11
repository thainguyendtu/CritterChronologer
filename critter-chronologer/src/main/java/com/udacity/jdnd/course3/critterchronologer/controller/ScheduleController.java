package com.udacity.jdnd.course3.critterchronologer.controller;

import com.udacity.jdnd.course3.critterchronologer.dto.response.ScheduleResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleResponseDTO createSchedule(@RequestBody ScheduleResponseDTO scheduleResponseDTO) {
        return scheduleService.createSchedule(scheduleResponseDTO);
    }

    @GetMapping
    public List<ScheduleResponseDTO> getAllSchedules() {
        return scheduleService.findAll();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleResponseDTO> getScheduleForPet(@PathVariable Long petId) {
        return scheduleService.getAllSchedulesForPet(petId);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleResponseDTO> getScheduleForEmployee(@PathVariable Long employeeId) {
        return scheduleService.getAllSchedulesForEmployee(employeeId);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleResponseDTO> getScheduleForCustomer(@PathVariable Long customerId) {
        return scheduleService.getAllScheduleForCustomer(customerId);
    }
}
