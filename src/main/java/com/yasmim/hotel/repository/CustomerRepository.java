package com.yasmim.hotel.repository;

import com.yasmim.hotel.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
