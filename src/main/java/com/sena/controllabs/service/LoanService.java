package com.sena.controllabs.service;

import java.util.List;
import java.util.Optional;

import com.sena.controllabs.entity.LoanEntity;

public interface LoanService {

    List<LoanEntity> findAll();

    Optional<LoanEntity> findById(Long id);

    LoanEntity save(LoanEntity loan);
}
