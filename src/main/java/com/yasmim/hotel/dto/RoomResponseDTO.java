package com.yasmim.hotel.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private String type;
    private BigDecimal price;
    private Integer capacity;
}

