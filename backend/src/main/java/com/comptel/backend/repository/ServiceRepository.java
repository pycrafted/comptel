package com.comptel.backend.repository;

import com.comptel.backend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository  extends JpaRepository<Service, Long> {
}
