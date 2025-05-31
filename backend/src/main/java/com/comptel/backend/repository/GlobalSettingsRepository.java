package com.comptel.backend.repository;

import com.comptel.backend.entity.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Long> {
}