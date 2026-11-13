package com.sena.controllabs.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.sena.controllabs.dto.RoomCreateRequest;
import com.sena.controllabs.dto.RoomResponse;
import com.sena.controllabs.entity.RoomEntity;
import com.sena.controllabs.service.RoomService;

import jakarta.validation.Valid;

@RestController
public class RoomController implements RoomControllerApi {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public ResponseEntity<List<RoomResponse>> getAllRooms(String block) {
        List<RoomEntity> rooms = block == null
                ? roomService.findAll()
                : roomService.findByBloque(block);

        List<RoomResponse> responseList = rooms.stream()
                .map(RoomResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<RoomResponse> getRoomById(UUID id) {
        return roomService.findById(id)
                .map(RoomResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<RoomResponse> createRoom(@Valid RoomCreateRequest request) {
        RoomEntity savedEntity = roomService.save(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RoomResponse.fromEntity(savedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteRoom(UUID id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
