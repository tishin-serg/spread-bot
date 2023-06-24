package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.RequestDetails;
import com.tishinserg.spreadbot.repository.GroupSubRepository;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-level testing for GroupSubServiceImpl")
class GroupSubServiceTest {

    @InjectMocks
    private GroupSubServiceImpl groupSubService;

    @Mock
    private GroupSubRepository groupSubRepository;

    @Mock
    private TelegramUserService telegramUserService;

    @Test
    void shouldProperlyFindById() {
        //given
        GroupSub givenGroupSub = new GroupSub();
        givenGroupSub.setId(1L);
        givenGroupSub.setService("unistream");
        givenGroupSub.setRequestDetails(new RequestDetails("GEO"));
        givenGroupSub.setCurrencyFrom("USD");
        givenGroupSub.setCurrencyTo("RUB");
        givenGroupSub.setLastRate(BigDecimal.valueOf(70.00));
        givenGroupSub.setUsers(new ArrayList<>());

        when(groupSubRepository.findById(1L)).thenReturn(Optional.of(givenGroupSub));

        //when
        Optional<GroupSub> groupSub = groupSubService.findById(1L);

        //then
        assertThat(groupSub).isPresent();
        assertThat(groupSub.get()).isEqualTo(givenGroupSub);
    }

    @Test
    void shouldProperlySave() {
        //given
        GroupSub givenGroupSub = new GroupSub();
        givenGroupSub.setId(1L);
        givenGroupSub.setService("unistream");
        givenGroupSub.setRequestDetails(new RequestDetails("GEO"));
        givenGroupSub.setCurrencyFrom("USD");
        givenGroupSub.setCurrencyTo("RUB");
        givenGroupSub.setLastRate(BigDecimal.valueOf(70.00));
        givenGroupSub.setUsers(new ArrayList<>());

        when(groupSubRepository.save(givenGroupSub)).thenReturn(givenGroupSub);

        //when
        GroupSub groupSub = groupSubService.save(givenGroupSub);

        //then
        assertThat(groupSub).isNotNull();
        assertThat(groupSub).isEqualTo(givenGroupSub);
    }

    @Test
    void shouldProperlySubscribe() {
        //given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setActive(true);
        telegramUser.setChatId("123456");
        List<GroupSub> groupSubList = new ArrayList<>();
        telegramUser.setGroupSubs(groupSubList);
        when(telegramUserService.findByChatId("123456")).thenReturn(Optional.of(telegramUser));

        GroupSub groupSub = new GroupSub();
        groupSub.setId(1L);
        groupSub.setService("unistream");
        groupSub.setRequestDetails(new RequestDetails("GEO"));
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("RUB");
        groupSub.setLastRate(BigDecimal.valueOf(100.00));
        List<TelegramUser> userList = new ArrayList<>();
        groupSub.setUsers(userList);
        when(groupSubRepository.findById(1L)).thenReturn(Optional.of(groupSub));

        //when
        groupSubService.subscribe("123456", 1L);

        //then
        verify(telegramUserService).save(telegramUser);
        verify(groupSubRepository).save(groupSub);
        assertThat(groupSub.getUsers().contains(telegramUser)).isTrue();
        assertThat(telegramUser.getGroupSubs().contains(groupSub)).isTrue();
    }

    @Test
    void shouldProperlyUnsubscribe() {
        //given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setActive(true);
        telegramUser.setChatId("123456");

        GroupSub groupSub = new GroupSub();
        groupSub.setId(1L);
        groupSub.setService("unistream");
        groupSub.setRequestDetails(new RequestDetails("GEO"));
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("RUB");
        groupSub.setLastRate(BigDecimal.valueOf(100.00));

        List<GroupSub> groupSubList = new ArrayList<>();
        telegramUser.getGroupSubs().add(groupSub);
        telegramUser.setGroupSubs(groupSubList);
        List<TelegramUser> userList = new ArrayList<>();
        groupSub.getUsers().add(telegramUser);
        groupSub.setUsers(userList);

        when(telegramUserService.findByChatId("123456")).thenReturn(Optional.of(telegramUser));
        when(groupSubRepository.findById(1L)).thenReturn(Optional.of(groupSub));

        //when
        groupSubService.unsubscribe("123456", 1L);

        //then
        verify(telegramUserService).save(telegramUser);
        verify(groupSubRepository).save(groupSub);
        assertThat(groupSub.getUsers().contains(telegramUser)).isFalse();
        assertThat(telegramUser.getGroupSubs().contains(groupSub)).isFalse();
    }

    @Test
    void shouldProperlyFindAllGroups() {
        //given
        GroupSub groupSub1 = new GroupSub();
        groupSub1.setId(1L);
        groupSub1.setService("unistream");
        groupSub1.setRequestDetails(new RequestDetails("GEO"));
        groupSub1.setCurrencyFrom("USD");
        groupSub1.setCurrencyTo("RUB");
        groupSub1.setLastRate(BigDecimal.valueOf(70.00));
        groupSub1.setUsers(new ArrayList<>());

        GroupSub groupSub2 = new GroupSub();
        groupSub2.setId(2L);
        groupSub2.setService("unistream");
        groupSub2.setRequestDetails(new RequestDetails("GEO"));
        groupSub2.setCurrencyFrom("GEL");
        groupSub2.setCurrencyTo("RUB");
        groupSub2.setLastRate(BigDecimal.valueOf(30.00));
        groupSub2.setUsers(new ArrayList<>());

        GroupSub groupSub3 = new GroupSub();
        groupSub3.setId(3L);
        groupSub3.setService("unistream");
        groupSub3.setRequestDetails(new RequestDetails("GEO"));
        groupSub3.setCurrencyFrom("EUR");
        groupSub3.setCurrencyTo("RUB");
        groupSub3.setLastRate(BigDecimal.valueOf(80.00));
        groupSub3.setUsers(new ArrayList<>());

        List<GroupSub> givenGroupSubList = new ArrayList<>();
        givenGroupSubList.add(groupSub1);
        givenGroupSubList.add(groupSub2);
        givenGroupSubList.add(groupSub3);

        when(groupSubRepository.findAll()).thenReturn(givenGroupSubList);

        //when
        List<GroupSub> groupSubList = groupSubService.findAllGroups();

        //then
        verify(groupSubRepository).findAll();
        assertThat(groupSubList).isNotNull();
        assertThat(groupSubList).isEqualTo(givenGroupSubList);
    }
}