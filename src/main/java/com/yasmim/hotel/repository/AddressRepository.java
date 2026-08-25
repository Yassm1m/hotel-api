package com.yasmim.hotel.repository;

import com.yasmim.hotel.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}