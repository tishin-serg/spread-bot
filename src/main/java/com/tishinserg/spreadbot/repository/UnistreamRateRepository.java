package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Repository} for handling with {@link UnistreamRate } entity.
 */
public interface UnistreamRateRepository extends JpaRepository<UnistreamRate, Long> {

    UnistreamRate findTopByOrderByIdDesc();

    @Query(value = "SELECT * FROM uni_rate u WHERE u.country = :country AND u.currency = :currency ORDER BY u.date DESC LIMIT 1",
            nativeQuery = true)
    UnistreamRate findLastRateCurrency(@Param("country") String country, @Param("currency") String currency);
}
