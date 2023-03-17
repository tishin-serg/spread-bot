package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * {@link Repository} for handling with {@link UnistreamRate } entity.
 */
public interface UnistreamRateRepository extends JpaRepository<UnistreamRate, Long> {

    UnistreamRate findTopByOrderByIdDesc();
}
