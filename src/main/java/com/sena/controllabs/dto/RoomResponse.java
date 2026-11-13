package com.sena.controllabs.dto;

import java.util.UUID;

import com.sena.controllabs.entity.RoomEntity;

public record RoomResponse(
        UUID id,
        String nombre,
        String bloque,
        Integer capacidadPcs,
        String estado
) {

    public static RoomResponse fromEntity(RoomEntity entity) {
        return new RoomResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getBloque(),
                entity.getCapacidadPcs(),
                entity.getEstado().name()
        );
    }
}
