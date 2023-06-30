package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.KoronaRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KoronaRateRepository extends JpaRepository<KoronaRate, Long> {

    @Query(value = "SELECT * FROM korona_rate k " +
            "WHERE k.country = :country " +
            "AND k.currency_from = :currencyFrom " +
            "AND k.currency_to = :currencyTo " +
            "ORDER BY k.date DESC LIMIT 1",
            nativeQuery = true)
    Optional<KoronaRate> findLastRateCurrency(@Param("country") String country,
                                              @Param("currencyFrom") String currencyFrom,
                                              @Param("currencyTo") String currencyTo);
}
