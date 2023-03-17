package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.repository.UnistreamRateRepository;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/uni_rate.sql"})
    @Test
    public void shouldProperlyFindTopByOrderByIdDesc() {
        //given
        UnistreamRate givenUnistreamRate = new UnistreamRate();
        givenUnistreamRate.setId(5L);
        givenUnistreamRate.setCurrency("RUB");
        givenUnistreamRate.setRate(77.22);
        givenUnistreamRate.setDate(LocalDateTime.of(2023, 3, 17, 0, 0, 0));

        //when
        UnistreamRate unistreamRate = unistreamRateRepository.findTopByOrderByIdDesc();

        //then
        Assertions.assertEquals(unistreamRate, givenUnistreamRate);
    }

    @Sql(scripts = {"/sql/clearDbs.sql"})
    @Test
    public void shouldProperlySaveUnistreamRate() {
        //given
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setCurrency("RUB");
        unistreamRate.setRate(77.22);
        unistreamRate.setDate(LocalDateTime.of(2023, 3, 17, 0, 0, 0));
        unistreamRateRepository.save(unistreamRate);

        //when
        Optional<UnistreamRate> saved = unistreamRateRepository.findById(unistreamRate.getId());

        //then
        Assertions.assertTrue(saved.isPresent());
        Assertions.assertEquals(unistreamRate, saved.get());
    }


}
