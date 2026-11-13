package com.sena.controllabs.dto;

import com.sena.controllabs.entity.RoomEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "El bloque es obligatorio")
        String locationBlock,

        @NotNull(message = "La capacidad de PCs es obligatoria")
        @Min(value = 1, message = "Mínimo 1 PC")
        Integer totalPcs
) {

    public RoomEntity toEntity() {
        RoomEntity entity = new RoomEntity();
        entity.setNombre(name);
        entity.setBloque(locationBlock);
        entity.setCapacidadPcs(totalPcs);
        entity.setEstado(RoomEntity.RoomStatus.OPERATIVO);
        return entity;
    }
}
