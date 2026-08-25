package com.yasmim.hotel.service;

import com.yasmim.hotel.dto.RoomRequestDTO;
import com.yasmim.hotel.dto.RoomResponseDTO;
import com.yasmim.hotel.model.Room;
import com.yasmim.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<RoomResponseDTO> findAll() {
        log.info("Buscando todos os rooms");
        return roomRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public RoomResponseDTO findById(Long id) {
        log.info("Buscando room com id {}", id);
        Room room = getRoomOrThrow(id);
        return toResponseDTO(room);
    }

    public RoomResponseDTO create(RoomRequestDTO dto) {
        log.info("Criando novo room: {}", dto.getRoomNumber());
        Room room = toEntity(dto);
        Room saved = roomRepository.save(room);
        log.info("Room criado com sucesso, id {}", saved.getId());
        return toResponseDTO(saved);
    }

    public RoomResponseDTO update(Long id, RoomRequestDTO dto) {
        log.info("Atualizando room com id {}", id);
        Room room = getRoomOrThrow(id);

        room.setRoomNumber(dto.getRoomNumber());
        room.setType(dto.getType());
        room.setPrice(dto.getPrice());
        room.setCapacity(dto.getCapacity());

        Room updated = roomRepository.save(room);
        log.info("Room id {} atualizado com sucesso", id);
        return toResponseDTO(updated);
    }

    public void delete(Long id) {
        log.info("Excluindo room com id {}", id);
        Room room = getRoomOrThrow(id);
        roomRepository.delete(room);
        log.info("Room id {} excluído com sucesso", id);
    }

    private Room getRoomOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Room com id {} não encontrado", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Room não encontrado");
                });
    }

    private Room toEntity(RoomRequestDTO dto) {
        return Room.builder()
                .roomNumber(dto.getRoomNumber())
                .type(dto.getType())
                .price(dto.getPrice())
                .capacity(dto.getCapacity())
                .build();
    }

    private RoomResponseDTO toResponseDTO(Room room) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType())
                .price(room.getPrice())
                .capacity(room.getCapacity())
                .build();
    }
}