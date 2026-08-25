package com.yasmim.hotel.repository;

import com.yasmim.hotel.model.Reservation;
import com.yasmim.hotel.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Busca reservas dentro de um intervalo de datas (check-in dentro do range)
    List<Reservation> findByCheckInBetween(LocalDate start, LocalDate end);

    // Busca reservas por status (ex: IN_USE, para saber quartos ocupados)
    List<Reservation> findByStatus(ReservationStatus status);

    // Busca a reserva ativa (IN_USE) de um quarto específico, se existir
    java.util.Optional<Reservation> findByRoomIdAndStatus(Long roomId, ReservationStatus status);
}
