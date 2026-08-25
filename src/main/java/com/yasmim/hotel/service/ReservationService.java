package com.yasmim.hotel.service;

import com.yasmim.hotel.dto.ReservationRequestDTO;
import com.yasmim.hotel.dto.ReservationResponseDTO;
import com.yasmim.hotel.model.Customer;
import com.yasmim.hotel.model.Reservation;
import com.yasmim.hotel.model.ReservationStatus;
import com.yasmim.hotel.model.Room;
import com.yasmim.hotel.repository.CustomerRepository;
import com.yasmim.hotel.repository.ReservationRepository;
import com.yasmim.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    public ReservationResponseDTO open(ReservationRequestDTO dto) {
        log.info("Abrindo nova reserva para customerId {} e roomId {}", dto.getCustomerId(), dto.getRoomId());

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> {
                    log.warn("Customer com id {} não encontrado ao abrir reserva", dto.getCustomerId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer não encontrado");
                });

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> {
                    log.warn("Room com id {} não encontrado ao abrir reserva", dto.getRoomId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Room não encontrado");
                });

        List<Reservation> activeReservations = reservationRepository.findByRoomIdAndStatusIn(
                room.getId(), List.of(ReservationStatus.OPEN, ReservationStatus.IN_USE));

        boolean hasConflict = activeReservations.stream()
                .anyMatch(r -> dto.getCheckIn().isBefore(r.getCheckOut()) && dto.getCheckOut().isAfter(r.getCheckIn()));

        if (hasConflict) {
            log.warn("Tentativa de reservar room id {} com conflito de datas ({} a {})", room.getId(), dto.getCheckIn(), dto.getCheckOut());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Room já possui reserva no período informado");
        }

        Reservation reservation = Reservation.builder()
                .customer(customer)
                .room(room)
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .status(ReservationStatus.OPEN)
                .build();

        Reservation saved = reservationRepository.save(reservation);
        log.info("Reserva aberta com sucesso, id {}", saved.getId());
        return toResponseDTO(saved);
    }

    public ReservationResponseDTO close(Long id) {
        log.info("Encerrando reserva id {}", id);
        Reservation reservation = getReservationOrThrow(id);

        if (reservation.getStatus() == ReservationStatus.FINISHED || reservation.getStatus() == ReservationStatus.CANCELLED) {
            log.warn("Reserva id {} já estava encerrada com status {}", id, reservation.getStatus());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Reserva já está encerrada");
        }

        reservation.setStatus(ReservationStatus.FINISHED);
        Reservation updated = reservationRepository.save(reservation);
        log.info("Reserva id {} encerrada com sucesso", id);
        return toResponseDTO(updated);
    }

    public List<ReservationResponseDTO> findByDateRange(LocalDate start, LocalDate end) {
        log.info("Buscando reservas entre {} e {}", start, end);
        return reservationRepository.findByCheckInBetween(start, end)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ReservationResponseDTO> findOccupiedRoomsNow() {
        log.info("Buscando quartos ocupados no momento");
        LocalDate today = LocalDate.now();
        return reservationRepository.findByStatus(ReservationStatus.OPEN)
                .stream()
                .filter(r -> !today.isBefore(r.getCheckIn()) && !today.isAfter(r.getCheckOut()))
                .map(this::toResponseDTO)
                .toList();
    }

    private Reservation getReservationOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reserva com id {} não encontrada", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada");
                });
    }

    private ReservationResponseDTO toResponseDTO(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .customerId(reservation.getCustomer().getId())
                .customerName(reservation.getCustomer().getName())
                .roomId(reservation.getRoom().getId())
                .roomNumber(reservation.getRoom().getRoomNumber())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}