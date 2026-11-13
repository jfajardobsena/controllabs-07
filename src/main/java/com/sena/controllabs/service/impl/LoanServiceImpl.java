package com.sena.controllabs.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sena.controllabs.entity.LoanEntity;
import com.sena.controllabs.repository.LoanRepository;
import com.sena.controllabs.service.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public List<LoanEntity> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Optional<LoanEntity> findById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public LoanEntity save(LoanEntity loan) {
        return loanRepository.save(loan);
    }
}
