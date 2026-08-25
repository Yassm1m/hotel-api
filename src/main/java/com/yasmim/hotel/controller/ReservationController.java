package com.yasmim.hotel.controller;

import com.yasmim.hotel.dto.ReservationRequestDTO;
import com.yasmim.hotel.dto.ReservationResponseDTO;
import com.yasmim.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/open")
    public ResponseEntity<ReservationResponseDTO> open(@RequestBody ReservationRequestDTO dto) {
        log.info("POST /api/reservations/open");
        ReservationResponseDTO created = reservationService.open(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<ReservationResponseDTO> close(@PathVariable Long id) {
        log.info("PUT /api/reservations/{}/close", id);
        return ResponseEntity.ok(reservationService.close(id));
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<ReservationResponseDTO>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        log.info("GET /api/reservations/by-date-range?start={}&end={}", start, end);
        return ResponseEntity.ok(reservationService.findByDateRange(start, end));
    }

    @GetMapping("/occupied-rooms")
    public ResponseEntity<List<ReservationResponseDTO>> occupiedRoomsNow() {
        log.info("GET /api/reservations/occupied-rooms");
        return ResponseEntity.ok(reservationService.findOccupiedRoomsNow());
    }
}