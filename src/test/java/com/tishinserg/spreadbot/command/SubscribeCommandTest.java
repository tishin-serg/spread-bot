package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import com.tishinserg.spreadbot.service.GroupSubService;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Unit-level testing for SubscribeCommand")
public class SubscribeCommandTest {

    private SubscribeCommand subscribeCommand;

    @Mock
    private SendBotMessageService sendBotMessageService;
    @Mock
    private GroupSubService groupSubService;
    @Mock
    private TelegramUserService telegramUserService;
    @Mock
    private Update update;
    @Mock
    private Message message;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        subscribeCommand = new SubscribeCommand(sendBotMessageService, groupSubService, telegramUserService);
    }

    //Не нашёл группу подписки - вернул сообщение что не найдено
    @Test
    public void shouldDoesNotExistByGroupId() {
        //given
        String chatId = "12345";
        Long groupId = 1234L;
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(Long.valueOf(chatId));
        when(message.getText()).thenReturn(CommandName.SUB.getCommandName() + " " + groupId);
        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.empty());

        String expectedMessage = String.format(SubscribeCommand.NOT_FOUND, groupId);

        //when
        subscribeCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }

    //Нашёл группу подписки - отправил сообщение, что подписал
    @Test
    public void shouldProperlySendSubscribeByGroupId() {
        //given
        String chatId = "12345";
        Long groupId = 1L;

//        GroupSub groupSub = GroupSub.builder()
//                .tittle("Юнистрим - Перевод в Грузию - USD/RUB")
//                .lastRate(BigDecimal.valueOf(70.00))
//                .build();
        GroupSub groupSub = new GroupSub();
        groupSub.setTittle("Юнистрим - Перевод в Грузию - USD/RUB");
        groupSub.setLastRate(BigDecimal.valueOf(70.00));

        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(Long.valueOf(chatId));
        when(message.getText()).thenReturn(CommandName.SUB.getCommandName() + " " + groupId);
        when(groupSubService.findById(groupId)).thenReturn(Optional.of(groupSub));
        when(groupSubService.subscribe(chatId, groupId)).thenReturn(groupSub);
        when(telegramUserService.findByChatId(chatId)).thenReturn(Optional.of(telegramUser));

        String subscribeName = groupSub.getTittle();
        String currentRate = String.format("%s руб.", groupSub.getLastRate());
        String expectedMessage = String.format(SubscribeCommand.SUB_MESSAGE, subscribeName, currentRate);

        //when
        subscribeCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }

    @Test
    void shouldSendListOfGroupsIfNoIdInMessage() {
        //given
        String chatId = "12345";
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(Long.valueOf(chatId));
        when(message.getText()).thenReturn(CommandName.SUB.getCommandName());

        // GroupSub groupSub = GroupSub.builder().id(1L).tittle("Юнистрим - Перевод в Грузию - USD/RUB").build();
        GroupSub groupSub = new GroupSub();
        groupSub.setTittle("Юнистрим - Перевод в Грузию - USD/RUB");
        groupSub.setId(1L);
        String groupId = String.format("%s - %s \n",
                groupSub.getTittle(),
                groupSub.getId());

        when(groupSubService.findAllGroups()).thenReturn(Collections.singletonList(groupSub));

        //when
        subscribeCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(chatId, String.format(SubscribeCommand.LIST_GROUPS, groupId));
    }

    @Test
    void shouldProperlySendMessageIfGroupIdInvalid() {
        //given
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(1234L);
        when(message.getText()).thenReturn("/subscribe foo");

        //when
        subscribeCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(eq("1234"), eq(SubscribeCommand.ID_IS_INVALID));
    }
}
