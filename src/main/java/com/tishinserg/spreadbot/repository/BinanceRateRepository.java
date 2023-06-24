package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.BinanceRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BinanceRateRepository extends JpaRepository<BinanceRate, Long> {

    @Query(value = "SELECT * FROM binance_rate b " +
            "WHERE b.payment_method = :paymentMethod " +
            "AND b.currency_from = :currencyFrom " +
            "AND b.currency_to = :currencyTo " +
            "AND b.trade_type = :tradeType " +
            "ORDER BY b.date DESC LIMIT 1",
            nativeQuery = true)
    Optional<BinanceRate> findLastRateCurrency(@Param("paymentMethod") String paymentMethod,
                                               @Param("currencyFrom") String currencyFrom,
                                               @Param("currencyTo") String currencyTo,
                                               @Param("tradeType") String tradeType);
}
