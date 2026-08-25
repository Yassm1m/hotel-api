package com.yasmim.hotel.service;

import com.yasmim.hotel.dto.ReservationRequestDTO;
import com.yasmim.hotel.model.Customer;
import com.yasmim.hotel.model.Reservation;
import com.yasmim.hotel.model.ReservationStatus;
import com.yasmim.hotel.model.Room;
import com.yasmim.hotel.repository.CustomerRepository;
import com.yasmim.hotel.repository.ReservationRepository;
import com.yasmim.hotel.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationOverbookingTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void deveRecusarReservaQuandoHaSobreposicaoDeDatas() {
        Customer customer = Customer.builder().id(1L).name("Maria Silva").build();
        Room room = Room.builder().id(1L).roomNumber("101").build();

        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .customerId(1L)
                .roomId(1L)
                .checkIn(LocalDate.of(2026, 9, 5))
                .checkOut(LocalDate.of(2026, 9, 15))
                .build();

        Reservation existente = Reservation.builder()
                .id(10L)
                .room(room)
                .customer(customer)
                .checkIn(LocalDate.of(2026, 9, 1))
                .checkOut(LocalDate.of(2026, 9, 10))
                .status(ReservationStatus.OPEN)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomIdAndStatusIn(anyLong(), any()))
                .thenReturn(List.of(existente));

        assertThatThrownBy(() -> reservationService.open(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("período");
    }
}