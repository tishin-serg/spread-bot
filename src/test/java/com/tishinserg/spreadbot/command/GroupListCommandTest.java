package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Unit-level testing for GroupListCommand")
@ExtendWith(MockitoExtension.class)
public class GroupListCommandTest {

    private GroupListCommand groupListCommand;

    @Mock
    private SendBotMessageService sendBotMessageService;

    @Mock
    private TelegramUserService telegramUserService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        groupListCommand = new GroupListCommand(sendBotMessageService, telegramUserService);
    }

    @Test
    void shouldProperlySendCorrectMessage() {
        //given
        String chatId = "123456";
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(Long.parseLong(chatId));
        when(update.getMessage()).thenReturn(message);

        GroupSub groupSub1 = new GroupSub();
        groupSub1.setId(1L);
        groupSub1.setService("unistream");
        groupSub1.setCountry("GEO");
        groupSub1.setCurrencyFrom("USD");
        groupSub1.setCurrencyTo("RUB");
        groupSub1.setTittle("Юнистрим - Перевод в Грузию - USD/RUB");
        GroupSub groupSub2 = new GroupSub();
        groupSub2.setId(2L);
        groupSub2.setService("unistream");
        groupSub2.setCountry("GEO");
        groupSub2.setCurrencyFrom("GEL");
        groupSub2.setCurrencyTo("RUB");
        groupSub2.setTittle("Юнистрим - Перевод в Грузию - GEL/RUB");
        List<GroupSub> groupSubs = Arrays.asList(
                groupSub1,
                groupSub2
        );
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId);
        telegramUser.setGroupSubs(groupSubs);
        when(telegramUserService.findByChatId(chatId)).thenReturn(Optional.of(telegramUser));

        //when
        groupListCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(chatId,
                "Я нашёл все подписки на курсы:\n" +
                        "Юнистрим - Перевод в Грузию - USD/RUB - 1\n" +
                        "Юнистрим - Перевод в Грузию - GEL/RUB - 2\n");
    }

    @Test
    void shouldProperlyReturnGroupSubs() {
        //given
        String chatId = "123456";
        GroupSub groupSub = new GroupSub();
        groupSub.setId(1L);
        groupSub.setService("unistream");
        groupSub.setCountry("GEO");
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("RUB");
        List<GroupSub> groupSubs = Collections.singletonList(groupSub);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId);
        telegramUser.setGroupSubs(groupSubs);
        when(telegramUserService.findByChatId(chatId)).thenReturn(Optional.of(telegramUser));

        //when
        List<GroupSub> result = groupListCommand.findGroupSubsForUser(chatId);

        //then
        assertThat(groupSubs).isEqualTo(result);
    }

    @Test
    void shouldProperlyMapGroupSubsToString() {
        //given
        String chatId = "12345";

        GroupSub groupSub1 = new GroupSub();
        groupSub1.setId(1L);
        groupSub1.setCountry("GEO");
        groupSub1.setCurrencyFrom("USD");
        groupSub1.setCurrencyTo("RUB");
        groupSub1.setService("unistream");
        groupSub1.setTittle("Юнистрим - Перевод в Грузию - USD/RUB");

        GroupSub groupSub2 = new GroupSub();
        groupSub2.setId(2L);
        groupSub2.setCountry("GEO");
        groupSub2.setCurrencyFrom("GEL");
        groupSub2.setCurrencyTo("RUB");
        groupSub2.setService("unistream");
        groupSub2.setTittle("Юнистрим - Перевод в Грузию - GEL/RUB");

        List<GroupSub> groupSubList = Arrays.asList(groupSub1, groupSub2);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setGroupSubs(groupSubList);
        telegramUser.setChatId(chatId);

        when(telegramUserService.findByChatId(chatId)).thenReturn(Optional.of(telegramUser));

        String expectedString = "Юнистрим - Перевод в Грузию - USD/RUB - 1\nЮнистрим - Перевод в Грузию - GEL/RUB - 2\n";

        //when
        String mappedString = groupListCommand.mapGroupList(chatId);

        //then
        assertThat(mappedString).isEqualTo(expectedString);
    }

    @Test
    void shouldProperlySendMessageIfSubListIsEmpty() {
        //given
        String chatId = "12345";
        when(telegramUserService.findByChatId(chatId)).thenReturn(Optional.of(new TelegramUser()));
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(Long.parseLong(chatId));
        when(update.getMessage()).thenReturn(message);

        //when
        groupListCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(chatId, "Cписок подписок пуст :(");
        verifyNoMoreInteractions(sendBotMessageService);

    }

}
