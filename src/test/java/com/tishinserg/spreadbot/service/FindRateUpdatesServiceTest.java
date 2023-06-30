package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    private RateService rateService;
    @Mock
    private RateServiceFactory rateServiceFactory;

//    @Test
//    public void shouldProperlyFindRateUpdates() {
//
//        // given
//        GroupSub groupSub1 = new GroupSub();
//        groupSub1.setId(1L);
//        GroupSub groupSub2 = new GroupSub();
//        groupSub2.setId(2L);
//        GroupSub groupSub3 = new GroupSub();
//        groupSub3.setId(3L);
//        List<GroupSub> groupSubs = new ArrayList<>(Arrays.asList(groupSub1, groupSub2, groupSub3));
//        when(groupSubService.findAllGroups()).thenReturn(groupSubs);
//        //todo
////        FindRateUpdatesServiceImpl spyService = Mockito.spy(new FindRateUpdatesServiceImpl(groupSubService,
////                sendBotMessageService, rateServiceFactory, unistreamRateService));
////
////        // when
////        spyService.findRateUpdates();
////
////        // then
////        verify(groupSubService, times(1)).findAllGroups();
////        verify(spyService, times(groupSubs.size())).compareRatesAndNotify(any(GroupSub.class));
//    }

    @Test
    public void shouldProperlySendMessageIfRateChange() throws InterruptedException {
        //given
        GroupSub groupSub = new GroupSub();
        groupSub.setService("unistream");
        when(rateServiceFactory.createInstance(groupSub.getService())).thenReturn(rateService);
        Rate currentRate = new Rate();
        currentRate.setRate(BigDecimal.valueOf(70));
        Rate lastRate = new Rate();
        lastRate.setRate(BigDecimal.valueOf(71));
        when(rateService.getCurrentRate(groupSub)).thenReturn(CompletableFuture.completedFuture(currentRate));
        when(rateService.getLastRate(groupSub)).thenReturn(Optional.of(lastRate));

        //when
        findRateUpdatesService.compareRatesAndNotify(groupSub);

        //then
        assertThat(groupSub.getLastRate()).isEqualTo(currentRate.getRate());
        verify(groupSubService).save(groupSub);
        verify(rateService).save(currentRate, groupSub);
    }


}