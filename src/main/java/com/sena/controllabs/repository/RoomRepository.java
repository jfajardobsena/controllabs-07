package com.sena.controllabs.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.controllabs.entity.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

    List<RoomEntity> findByBloque(String bloque);

    List<RoomEntity> findByBloqueAndEstado(String bloque, RoomEntity.RoomStatus estado);
}
