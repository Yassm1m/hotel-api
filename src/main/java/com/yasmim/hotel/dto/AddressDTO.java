package com.yasmim.hotel.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private Long id;
    private String zipCode;
    private String street;
    private String addressDetails;
    private String neighborhood;
    private String state;
}