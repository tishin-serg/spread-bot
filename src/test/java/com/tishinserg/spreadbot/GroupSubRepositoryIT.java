package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.repository.GroupSubRepository;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupSubRepositoryIT {

    @Autowired
    private GroupSubRepository groupSubRepository;

    @Sql(scripts = {"/sql/clear_group_sub.sql", "/sql/group_sub.sql"})
    @Test
    public void shouldProperlyFindAll() {
        //when

        List<GroupSub> groupSubList = groupSubRepository.findAll();

        //then
        assertThat(groupSubList).isNotNull();
        assertThat(groupSubList.size()).isEqualTo(5);
    }

    @Sql(scripts = {"/sql/clear_group_sub.sql", "/sql/group_sub.sql"})
    @Test
    public void shouldProperlyFindById() {
        //given

        GroupSub givenGroupSub = new GroupSub();
        givenGroupSub.setId(3L);
        givenGroupSub.setService("unistream");
        givenGroupSub.setCountry("GEO");
        givenGroupSub.setCurrencyFrom("USD");
        givenGroupSub.setCurrencyTo("RUB");
        givenGroupSub.setTittle("Юнистрим - Перевод в Грузию - USD/RUB");
        givenGroupSub.setLastRate(BigDecimal.valueOf(10.03));

        //when
        Optional<GroupSub> groupSub = groupSubRepository.findById(3L);

        //then
        assertThat(groupSub).isPresent();
        assertThat(givenGroupSub).isEqualTo(groupSub.get());
    }

}
