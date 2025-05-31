package com.comptel.backend.repository;

import com.comptel.backend.entity.Exit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExitRepository extends JpaRepository<Exit, Long> {
    List<Exit> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Exit> findByTypeDepense(Exit.TypeDepense typeDepense);
}