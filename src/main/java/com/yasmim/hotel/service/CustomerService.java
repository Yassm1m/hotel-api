package com.yasmim.hotel.service;

import com.yasmim.hotel.dto.AddressDTO;
import com.yasmim.hotel.dto.CustomerRequestDTO;
import com.yasmim.hotel.dto.CustomerResponseDTO;
import com.yasmim.hotel.model.Address;
import com.yasmim.hotel.model.Customer;
import com.yasmim.hotel.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerResponseDTO> findAll() {
        log.info("Buscando todos os customers");
        return customerRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CustomerResponseDTO findById(Long id) {
        log.info("Buscando customer com id {}", id);
        Customer customer = getCustomerOrThrow(id);
        return toResponseDTO(customer);
    }

    public CustomerResponseDTO create(CustomerRequestDTO dto) {
        log.info("Criando novo customer: {}", dto.getEmail());
        Customer customer = toEntity(dto);
        Customer saved = customerRepository.save(customer);
        log.info("Customer criado com sucesso, id {}", saved.getId());
        return toResponseDTO(saved);
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {
        log.info("Atualizando customer com id {}", id);
        Customer customer = getCustomerOrThrow(id);

        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setCpf(dto.getCpf());

        if (dto.getAddress() != null) {
            if (customer.getAddress() == null) {
                customer.setAddress(new Address());
            }
            AddressDTO addressDTO = dto.getAddress();
            customer.getAddress().setZipCode(addressDTO.getZipCode());
            customer.getAddress().setStreet(addressDTO.getStreet());
            customer.getAddress().setAddressDetails(addressDTO.getAddressDetails());
            customer.getAddress().setNeighborhood(addressDTO.getNeighborhood());
            customer.getAddress().setState(addressDTO.getState());
        }

        Customer updated = customerRepository.save(customer);
        log.info("Customer id {} atualizado com sucesso", id);
        return toResponseDTO(updated);
    }

    public void delete(Long id) {
        log.info("Excluindo customer com id {}", id);
        Customer customer = getCustomerOrThrow(id);
        customerRepository.delete(customer);
        log.info("Customer id {} exclu\u00eddo com sucesso", id);
    }

    private Customer getCustomerOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer com id {} n\u00e3o encontrado", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer n\u00e3o encontrado");
                });
    }

    private Customer toEntity(CustomerRequestDTO dto) {
        Address address = null;
        if (dto.getAddress() != null) {
            address = Address.builder()
                    .zipCode(dto.getAddress().getZipCode())
                    .street(dto.getAddress().getStreet())
                    .addressDetails(dto.getAddress().getAddressDetails())
                    .neighborhood(dto.getAddress().getNeighborhood())
                    .state(dto.getAddress().getState())
                    .build();
        }

        return Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .cpf(dto.getCpf())
                .address(address)
                .build();
    }

    private CustomerResponseDTO toResponseDTO(Customer customer) {
        AddressDTO addressDTO = null;
        if (customer.getAddress() != null) {
            addressDTO = AddressDTO.builder()
                    .id(customer.getAddress().getId())
                    .zipCode(customer.getAddress().getZipCode())
                    .street(customer.getAddress().getStreet())
                    .addressDetails(customer.getAddress().getAddressDetails())
                    .neighborhood(customer.getAddress().getNeighborhood())
                    .state(customer.getAddress().getState())
                    .build();
        }

        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .cpf(customer.getCpf())
                .address(addressDTO)
                .build();
    }
}