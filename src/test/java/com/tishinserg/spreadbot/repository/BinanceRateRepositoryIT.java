package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.BinanceRateRepository;
import com.tishinserg.spreadbot.repository.entity.BinanceRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BinanceRateRepositoryIT {

    @Autowired
    private BinanceRateRepository binanceRateRepository;

    @Sql(scripts = {"/sql/clear_binance_rate.sql", "/sql/binance_rate.sql"})
    @Test
    public void shouldProperlyFindLastRateCurrency() {
        //given
        BinanceRate expected = new BinanceRate();
        expected.setPaymentMethod("Tinkoff");
        expected.setTradeType("SELL");
        expected.setCurrencyFrom("USD");
        expected.setCurrencyTo("RUB");
        expected.setRate(BigDecimal.valueOf(78.44));
        expected.setDate(LocalDateTime.of(2022, 1, 3, 15, 58, 0));

        //when
        Optional<BinanceRate> actual = binanceRateRepository.findLastRateCurrency("Tinkoff", "USD", "RUB", "SELL");

        //then
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getRate(), expected.getRate());
        assertEquals(actual.get().getDate(), expected.getDate());
    }
}
