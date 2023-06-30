package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.KoronaRate;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration-level testing for {@link KoronaRateRepositoryIT}.
 */

@Profile("test")
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class KoronaRateRepositoryIT {
    @Autowired
    private KoronaRateRepository koronaRateRepository;

    @Sql(scripts = {"/sql/clear_korona_rate.sql", "/sql/korona_rate.sql"})
    @Test
    public void shouldProperlyFindLastRateCurrency() {
        //given
        KoronaRate givenKoronaRate = new KoronaRate();
        givenKoronaRate.setId(2L);
        givenKoronaRate.setCurrencyFrom("840");
        givenKoronaRate.setCurrencyTo("810");
        givenKoronaRate.setCountry("GEO");
        givenKoronaRate.setRate(BigDecimal.valueOf(77.01));
        givenKoronaRate.setDate(LocalDateTime.of(2023, 3, 16, 23, 10, 0));

        //when
        KoronaRate koronaRate = koronaRateRepository.findLastRateCurrency("GEO", "840", "810").get();

        //then
        assertThat(koronaRate).isEqualTo(givenKoronaRate);
    }

    @Sql(scripts = {"/sql/clear_korona_rate.sql"})
    @Test
    public void shouldProperlySaveKoronaRate() {
        //given
        KoronaRate koronaRate = new KoronaRate();
        koronaRate.setCurrencyFrom("840");
        koronaRate.setCurrencyTo("810");
        koronaRate.setCountry("GEO");
        koronaRate.setRate(BigDecimal.valueOf(77.01));
        koronaRate.setDate(LocalDateTime.of(2023, 3, 16, 23, 10, 0));
        koronaRateRepository.save(koronaRate);

        //when
        Optional<KoronaRate> saved = koronaRateRepository.findById(koronaRate.getId());

        //then
        assertThat(saved.isPresent()).isTrue();
        assertThat(koronaRate).isEqualTo(saved.get());
    }
}
