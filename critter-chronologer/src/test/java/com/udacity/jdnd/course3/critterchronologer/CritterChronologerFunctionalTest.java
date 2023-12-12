package com.udacity.jdnd.course3.critterchronologer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critterchronologer.controller.PetController;
import com.udacity.jdnd.course3.critterchronologer.controller.ScheduleController;
import com.udacity.jdnd.course3.critterchronologer.controller.UserController;
import com.udacity.jdnd.course3.critterchronologer.dto.request.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critterchronologer.dto.response.CustomerResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.dto.response.EmployeeResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.dto.response.PetResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.dto.response.ScheduleResponseDTO;
import com.udacity.jdnd.course3.critterchronologer.enums.EmployeeSkill;
import com.udacity.jdnd.course3.critterchronologer.enums.PetType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest(classes = CritterChronologerApplication.class)
public class CritterChronologerFunctionalTest {

    @Autowired
    private UserController userController;

    @Autowired
    private PetController petController;

    @Autowired
    private ScheduleController scheduleController;

    @Test
    public void testCreateCustomer(){
        CustomerResponseDTO customerResponseDTO = createCustomerDTO();
        CustomerResponseDTO newCustomer = userController.saveCustomer(customerResponseDTO);
        CustomerResponseDTO retrievedCustomer = userController.getAllCustomers().get(0);

        Assertions.assertEquals(newCustomer.getName(), customerResponseDTO.getName());
        Assertions.assertEquals(newCustomer.getId(), retrievedCustomer.getId());
        Assertions.assertTrue(retrievedCustomer.getId() > 0);
    }

    @Test
    public void testCreateEmployee(){
        EmployeeResponseDTO employeeDTO = createEmployeeDTO();
        EmployeeResponseDTO newEmployee = userController.saveEmployee(employeeDTO);
        EmployeeResponseDTO retrievedEmployee = userController.getEmployee(newEmployee.getId());

        Assertions.assertEquals(employeeDTO.getSkills(), newEmployee.getSkills());
        Assertions.assertEquals(newEmployee.getId(), retrievedEmployee.getId());
        Assertions.assertTrue(retrievedEmployee.getId() > 0);
    }

    @Test
    public void testAddPetsToCustomer() {
        CustomerResponseDTO customerDTO = createCustomerDTO();
        CustomerResponseDTO newCustomer = userController.saveCustomer(customerDTO);

        PetResponseDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetResponseDTO newPet = petController.savePet(petDTO);

        //make sure pet contains customer id
        PetResponseDTO retrievedPet = petController.getPet(newPet.getId());
        Assertions.assertEquals(retrievedPet.getId(), newPet.getId());
        Assertions.assertEquals(retrievedPet.getOwnerId(), newCustomer.getId());

        // make sure you can retrieve pets by owner
        List<PetResponseDTO> pets = petController.getPetsByOwner(newCustomer.getId());
        Assertions.assertEquals(newPet.getId(), pets.get(0).getId());
        Assertions.assertEquals(newPet.getName(), pets.get(0).getName());

        // check to make sure customer now also contains pet
        CustomerResponseDTO retrievedCustomer = userController.getAllCustomers().get(0);
        Assertions.assertTrue(
                retrievedCustomer.getPetIds() != null && retrievedCustomer.getPetIds().size() > 0);
        Assertions.assertEquals(retrievedCustomer.getPetIds().get(0), retrievedPet.getId());
    }

    @Test
    public void testFindPetsByOwner() {
        CustomerResponseDTO customerDTO = createCustomerDTO();
        CustomerResponseDTO newCustomer = userController.saveCustomer(customerDTO);

        PetResponseDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetResponseDTO newPet = petController.savePet(petDTO);
        petDTO.setType(PetType.DOG);
        petDTO.setName("DogName");
        PetResponseDTO newPet2 = petController.savePet(petDTO);
        PetResponseDTO newPet3 = petController.savePet(petDTO);
        PetResponseDTO newPet4 = petController.savePet(petDTO);
        PetResponseDTO newPet5 = petController.savePet(petDTO);

        List<PetResponseDTO> pets = petController.getPetsByOwner(newCustomer.getId());
        Assertions.assertEquals(pets.size(), 5);
        Assertions.assertEquals(pets.get(0).getOwnerId(), newCustomer.getId());
        Assertions.assertEquals(pets.get(0).getId(), newPet.getId());
    }

    @Test
    public void testFindOwnerByPet() {
        CustomerResponseDTO customerDTO = createCustomerDTO();
        CustomerResponseDTO newCustomer = userController.saveCustomer(customerDTO);

        PetResponseDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetResponseDTO newPet = petController.savePet(petDTO);

        CustomerResponseDTO owner = userController.getPetOwner(newPet.getId());
        Assertions.assertEquals(owner.getId(), newCustomer.getId());
        Assertions.assertEquals(owner.getPetIds().get(0), newPet.getId());
    }

    @Test
    public void testChangeEmployeeAvailability() {
        EmployeeResponseDTO employeeDTO = createEmployeeDTO();
        EmployeeResponseDTO employee1 = userController.saveEmployee(employeeDTO);
        Assertions.assertNull(employee1.getDaysAvailable());

        Set<DayOfWeek> availability = Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
        userController.setAvailability(employee1.getId(), availability);

        EmployeeResponseDTO employee2 = userController.getEmployee(employee1.getId());
        Assertions.assertEquals(availability, employee2.getDaysAvailable());
    }

    @Test
    public void testFindEmployeesByServiceAndTime() {
        EmployeeResponseDTO employee1 = createEmployeeDTO();
        EmployeeResponseDTO employee2 = createEmployeeDTO();
        EmployeeResponseDTO employee3 = createEmployeeDTO();

        employee1.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        employee2.setDaysAvailable(Sets.newHashSet(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        employee3.setDaysAvailable(Sets.newHashSet(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

        employee1.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
        employee2.setSkills(Sets.newHashSet(EmployeeSkill.PETTING, EmployeeSkill.WALKING));
        employee3.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));

        EmployeeResponseDTO employee1DB = userController.saveEmployee(employee1);
        EmployeeResponseDTO employee2DB = userController.saveEmployee(employee2);
        EmployeeResponseDTO employee3DB = userController.saveEmployee(employee3);

        // make a request that matches employee 1 or 2
        EmployeeRequestDTO employeeRequest1DTO = new EmployeeRequestDTO();
        employeeRequest1DTO.setDate(LocalDate.of(2019, 12, 25));
        employeeRequest1DTO.setSkills(Sets.newHashSet(EmployeeSkill.PETTING));

        Set<Long> employeeIds1DBList = userController.findEmployeesForService(employeeRequest1DTO)
                                                     .stream()
                                                     .map(EmployeeResponseDTO::getId)
                                                     .collect(Collectors.toSet());

        Set<Long> employeeIds1ExpectedList = Sets.newHashSet(employee1DB.getId(), employee2DB.getId());
        Assertions.assertEquals(employeeIds1DBList, employeeIds1ExpectedList);

        // make a request that matches only employee 3
        EmployeeRequestDTO employeeRequest2DTO = new EmployeeRequestDTO();
        employeeRequest2DTO.setDate(LocalDate.of(2019, 12, 27));
        employeeRequest2DTO.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));

        Set<Long> employeeIds2DBList = userController.findEmployeesForService(employeeRequest2DTO)
                                                     .stream()
                                                     .map(EmployeeResponseDTO::getId)
                                                     .collect(Collectors.toSet());

        Set<Long> employeeIds2ExpectedList = Sets.newHashSet(employee3DB.getId());
        Assertions.assertEquals(employeeIds2DBList, employeeIds2ExpectedList);
    }

    @Test
    public void testSchedulePetsForServiceWithEmployee() {
        EmployeeResponseDTO employeeTemp = createEmployeeDTO();
        employeeTemp.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        EmployeeResponseDTO employeeDTO = userController.saveEmployee(employeeTemp);

        CustomerResponseDTO customerDTO = userController.saveCustomer(createCustomerDTO());
        PetResponseDTO petTemp = createPetDTO();
        petTemp.setOwnerId(customerDTO.getId());
        PetResponseDTO petDTO = petController.savePet(petTemp);

        LocalDate date = LocalDate.of(2019, 12, 25);
        List<Long> petList = Lists.newArrayList(petDTO.getId());
        List<Long> employeeList = Lists.newArrayList(employeeDTO.getId());
        Set<EmployeeSkill> skillSet =  Sets.newHashSet(EmployeeSkill.PETTING);

        scheduleController.createSchedule(createScheduleDTO(petList, employeeList, date, skillSet));
        ScheduleResponseDTO scheduleDTO = scheduleController.getAllSchedules().get(0);

        Assertions.assertEquals(scheduleDTO.getSkills(), skillSet);
        Assertions.assertEquals(scheduleDTO.getDate(), date);
        Assertions.assertEquals(scheduleDTO.getEmployeeIds(), employeeList);
        Assertions.assertEquals(scheduleDTO.getPetIds(), petList);
    }

    @Test
    public void testFindScheduleByEntities() {
        ScheduleResponseDTO schedule1 = createSchedule(1,
                                                      2,
                                                      LocalDate.of(2019, 12, 25),
                                                      Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.WALKING));

        ScheduleResponseDTO schedule2 = createSchedule(3,
                                                       1,
                                                       LocalDate.of(2019, 12, 26),
                                                       Sets.newHashSet(EmployeeSkill.PETTING));

        // add a third schedule that shares some employees and pets with the other schedules
        ScheduleResponseDTO schedule3 = new ScheduleResponseDTO();
        schedule3.setEmployeeIds(schedule1.getEmployeeIds());
        schedule3.setPetIds(schedule2.getPetIds());
        schedule3.setSkills(Sets.newHashSet(EmployeeSkill.SHAVING, EmployeeSkill.PETTING));
        schedule3.setDate(LocalDate.of(2020, 3, 23));

        scheduleController.createSchedule(schedule3);

        /*
            We now have 3 schedule entries. The third schedule entry has the same employees as the 1st schedule
            and the same pets/owners as the second schedule. So if we look up schedule entries for the employee from
            schedule 1, we should get both the first and third schedule as our result.
         */

        // Employee 1 in is both schedule 1 and 3
        List<ScheduleResponseDTO> schedulesContainEmployee1 =
                scheduleController.getScheduleForEmployee(schedule1.getEmployeeIds().get(0));
        compareSchedules(schedule1, schedulesContainEmployee1.get(0));
        compareSchedules(schedule3, schedulesContainEmployee1.get(1));

        // Employee 2 is only in schedule 2
        List<ScheduleResponseDTO> schedulesContainEmployee2 =
                scheduleController.getScheduleForEmployee(schedule2.getEmployeeIds().get(0));
        compareSchedules(schedule2, schedulesContainEmployee2.get(0));

        // Pet 1 is only in schedule 1
        List<ScheduleResponseDTO> schedulesContainPet1 =
                scheduleController.getScheduleForPet(schedule1.getPetIds().get(0));
        compareSchedules(schedule1, schedulesContainPet1.get(0));

        // Pet from schedule 2 is in both schedules 2 and 3
        List<ScheduleResponseDTO> schedulesContainPet2 =
                scheduleController.getScheduleForPet(schedule2.getPetIds().get(0));
        compareSchedules(schedule2, schedulesContainPet2.get(0));
        compareSchedules(schedule3, schedulesContainPet2.get(1));

        // Owner of the first pet will only be in schedule 1
        List<ScheduleResponseDTO> schedulesContainPet1Owner =
                scheduleController.getScheduleForCustomer(userController.getPetOwner(schedule1.getPetIds().get(0)).getId());
        compareSchedules(schedule1, schedulesContainPet1Owner.get(0));

        // Owner of pet from schedule 2 will be in both schedules 2 and 3
        List<ScheduleResponseDTO> scheduleResult =
                scheduleController.getScheduleForCustomer(userController.getPetOwner(schedule2.getPetIds().get(0)).getId());
        compareSchedules(schedule2, scheduleResult.get(0));
        compareSchedules(schedule3, scheduleResult.get(1));
    }

    private CustomerResponseDTO createCustomerDTO() {
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setName("TestEmployee");
        customerResponseDTO.setPhoneNumber("123-456-789");

        return customerResponseDTO;
    }

    private EmployeeResponseDTO createEmployeeDTO() {
        EmployeeResponseDTO employeeDTO = new EmployeeResponseDTO();
        employeeDTO.setName("TestEmployee");
        employeeDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));

        return employeeDTO;
    }

    private PetResponseDTO createPetDTO() {
        PetResponseDTO petDTO = new PetResponseDTO();
        petDTO.setName("TestPet");
        petDTO.setType(PetType.CAT);

        return petDTO;
    }

    private ScheduleResponseDTO createScheduleDTO(List<Long> petIds,
                                                         List<Long> employeeIds,
                                                         LocalDate date,
                                                         Set<EmployeeSkill> activities) {
        ScheduleResponseDTO scheduleDTO = new ScheduleResponseDTO();
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setDate(date);
        scheduleDTO.setSkills(activities);

        return scheduleDTO;
    }

    private ScheduleResponseDTO createSchedule(int numEmployees, int numPets, LocalDate date, Set<EmployeeSkill> skills) {
        List<Long> employeeIds = IntStream.range(0, numEmployees)
                                          .mapToObj(i -> createEmployeeDTO())
                                          .map(e -> {
                                              e.setSkills(skills);
                                              e.setDaysAvailable(Sets.newHashSet(date.getDayOfWeek()));
                                              return userController.saveEmployee(e).getId();
                                          })
                                          .collect(Collectors.toList());

        CustomerResponseDTO customer = userController.saveCustomer(createCustomerDTO());

        List<Long> petIds = IntStream.range(0, numPets)
                                     .mapToObj(i -> createPetDTO())
                                     .map(p -> {
                                         p.setOwnerId(customer.getId());
                                         return petController.savePet(p).getId();
                                     })
                                     .collect(Collectors.toList());

        return scheduleController.createSchedule(createScheduleDTO(petIds, employeeIds, date, skills));
    }

    private void compareSchedules(ScheduleResponseDTO schedule1, ScheduleResponseDTO schedule2) {
        Assertions.assertEquals(schedule1.getPetIds(), schedule2.getPetIds());
        Assertions.assertEquals(schedule1.getSkills(), schedule2.getSkills());
        Assertions.assertEquals(schedule1.getEmployeeIds(), schedule2.getEmployeeIds());
        Assertions.assertEquals(schedule1.getDate(), schedule2.getDate());
    }
}
