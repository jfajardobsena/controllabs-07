package com.sena.controllabs.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sena.controllabs.entity.LoanEntity;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    List<LoanEntity> findByRoomIdOrderByCreatedAtDesc(UUID roomId);

    @Query("SELECT l FROM LoanEntity l JOIN l.room r WHERE r.nombre = :roomName")
    List<LoanEntity> findByRoomName(@Param("roomName") String roomName);
}
