package com.yasmim.hotel.repository;

import com.yasmim.hotel.model.Reservation;
import com.yasmim.hotel.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCheckInBetween(LocalDate start, LocalDate end);
    List<Reservation> findByStatus(ReservationStatus status);
    java.util.Optional<Reservation> findByRoomIdAndStatus(Long roomId, ReservationStatus status);
    List<Reservation> findByRoomIdAndStatusIn(Long roomId, List<ReservationStatus> statuses);
}