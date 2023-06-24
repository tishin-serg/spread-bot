package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
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
 * Integration-level testing for {@link UnistreamRateRepositoryIT}.
 */

@Profile("test")
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UnistreamRateRepositoryIT {

    @Autowired
    private UnistreamRateRepository unistreamRateRepository;

    @Sql(scripts = {"/sql/clear_uni_rate.sql", "/sql/uni_rate.sql"})
    @Test
    public void shouldProperlyFindLastRateCurrency() {
        //given
        UnistreamRate givenUnistreamRate = new UnistreamRate();
        givenUnistreamRate.setId(2L);
        givenUnistreamRate.setCurrencyFrom("USD");
        givenUnistreamRate.setCurrencyTo("RUB");
        givenUnistreamRate.setCountry("GEO");
        givenUnistreamRate.setRate(BigDecimal.valueOf(77.01));
        givenUnistreamRate.setDate(LocalDateTime.of(2023, 3, 16, 23, 10, 0));

        //when
        UnistreamRate unistreamRate = unistreamRateRepository.findLastRateCurrency("GEO", "USD", "RUB").get();

        //then
        assertThat(unistreamRate).isEqualTo(givenUnistreamRate);
    }

    @Sql(scripts = {"/sql/clear_uni_rate.sql"})
    @Test
    public void shouldProperlySaveUnistreamRate() {
        //given
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setCurrencyFrom("USD");
        unistreamRate.setCurrencyTo("RUB");
        unistreamRate.setCountry("GEO");
        unistreamRate.setRate(BigDecimal.valueOf(77.22));
        unistreamRate.setDate(LocalDateTime.of(2023, 3, 17, 0, 0, 0));
        unistreamRateRepository.save(unistreamRate);

        //when
        Optional<UnistreamRate> saved = unistreamRateRepository.findById(unistreamRate.getId());

        //then
        assertThat(saved.isPresent()).isTrue();
        assertThat(unistreamRate).isEqualTo(saved.get());
    }


}
