package com.comptel.backend.repository;

import com.comptel.backend.entity.Input;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InputRepository extends JpaRepository<Input, Long> {
    List<Input> findByCreatedAtsBetween(LocalDateTime start, LocalDateTime end);
    List<Input> findByModePaiement(Input.ModePaiement modePaiement);
    List<Input> findBySaveBy_Id(Long userId);
}