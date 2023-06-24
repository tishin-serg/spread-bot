package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * {@link Repository} for handling with {@link UnistreamRate } entity.
 */
public interface UnistreamRateRepository extends JpaRepository<UnistreamRate, Long> {

    UnistreamRate findTopByOrderByIdDesc();

    @Query(value = "SELECT * FROM uni_rate u " +
            "WHERE u.country = :country " +
            "AND u.currency_from = :currencyFrom " +
            "AND u.currency_to = :currencyTo " +
            "ORDER BY u.date DESC LIMIT 1",
            nativeQuery = true)
    Optional<UnistreamRate> findLastRateCurrency(@Param("country") String country,
                                                @Param("currencyFrom") String currencyFrom,
                                                @Param("currencyTo") String currencyTo);
}
