package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-level testing for FindRateUpdatesService")
public class FindRateUpdatesServiceTest {

    @InjectMocks
    private FindRateUpdatesServiceImpl findRateUpdatesService;
    @Mock
    private GroupSubService groupSubService;
    @Mock
    private SendBotMessageService sendBotMessageService;
    @Mock
    private UnistreamRateService unistreamRateService;

    @Test
    public void shouldProperlyFindRateUpdates() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("123");
        telegramUser.setActive(true);
        List<TelegramUser> telegramUsers = new ArrayList<>();
        telegramUsers.add(telegramUser);
        List<GroupSub> groupSubs = new ArrayList<>();
        GroupSub groupSub = new GroupSub();
        groupSub.setCountry("GEO");
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("RUB");
        groupSub.setUsers(telegramUsers);
        groupSubs.add(groupSub);

        UnistreamRate currentRate = new UnistreamRate();
        currentRate.setCountry("GEO");
        currentRate.setCurrency("USD");
        currentRate.setDate(LocalDateTime.now());
        currentRate.setRate(BigDecimal.valueOf(77.05));

        UnistreamRate lastRate = new UnistreamRate();
        lastRate.setCountry("GEO");
        lastRate.setCurrency("USD");
        lastRate.setDate(LocalDateTime.now().minusDays(1));
        lastRate.setRate(BigDecimal.valueOf(77.00));

        when(groupSubService.findAllGroups()).thenReturn(groupSubs);
        when(unistreamRateService.getCurrentRate("GEO", "USD", "RUB")).thenReturn(currentRate);
        when(unistreamRateService.getLastRate("GEO", "USD")).thenReturn(lastRate);

        //when
        findRateUpdatesService.findRateUpdates();

        //then

        verify(unistreamRateService).compareUnistreamRates(lastRate, currentRate);
        verify(unistreamRateService).save(currentRate);


    }


}