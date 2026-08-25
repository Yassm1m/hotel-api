package com.yasmim.hotel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "address_details")
    private String addressDetails;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "state", length = 2)
    private String state;
}