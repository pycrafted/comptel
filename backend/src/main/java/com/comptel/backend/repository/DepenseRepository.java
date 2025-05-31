package com.comptel.backend.repository;

import com.comptel.backend.entity.Depense;
import com.comptel.backend.entity.Exit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {
    List<Depense> findByDateDepenseBetween(LocalDateTime start, LocalDateTime end);
    List<Depense> findByType(Exit.TypeDepense type);
}