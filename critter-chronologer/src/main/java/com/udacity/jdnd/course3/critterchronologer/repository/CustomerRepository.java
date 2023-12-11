package com.udacity.jdnd.course3.critterchronologer.repository;

import com.udacity.jdnd.course3.critterchronologer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
