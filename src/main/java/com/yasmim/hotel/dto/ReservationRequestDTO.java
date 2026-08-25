package com.yasmim.hotel.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {
    private Long customerId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
}