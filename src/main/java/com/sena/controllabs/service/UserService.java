package com.sena.controllabs.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sena.controllabs.entity.UserEntity;

public interface UserService {

    List<UserEntity> findAll();

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByEmail(String email);

    UserEntity save(UserEntity user);
}
