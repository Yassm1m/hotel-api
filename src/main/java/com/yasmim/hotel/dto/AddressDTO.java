package com.yasmim.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private Long id;

    @NotBlank(message = "CEP é obrigatório")
    private String zipCode;

    @NotBlank(message = "Rua é obrigatória")
    private String street;

    private String addressDetails;
    private String neighborhood;
    private String state;
}