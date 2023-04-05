package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.service.GroupSubService;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Unit-level testing for UnsubscribeCommand")
@ExtendWith(MockitoExtension.class)
public class UnsubscribeCommandTest {

    private UnsubscribeCommand unsubscribeCommand;

    @Mock
    private SendBotMessageService sendBotMessageService;

    @Mock
    private GroupSubService groupSubService;

    @Mock
    private CommandContainer commandContainer;

    @Mock
    private Message message;

    @Mock
    private Update update;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        unsubscribeCommand = new UnsubscribeCommand(sendBotMessageService, groupSubService, commandContainer);
    }

    @Test
    void shouldProperlyExecuteUnsubscribeCommandWithExistingGroupSub() {
        //given
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(1234L);
        when(message.getText()).thenReturn("/unsubscribe 1");
        GroupSub groupSub = new GroupSub();
        groupSub.setTittle("Юнистрим - Перевод в Грузию - USD/RUB");
        when(groupSubService.findById(1L)).thenReturn(Optional.of(groupSub));
        when(groupSubService.unsubscribe("1234", 1L)).thenReturn(groupSub);

        //when
        unsubscribeCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(eq("1234"), eq(String.format(UnsubscribeCommand.UN_SUB_MESSAGE,
                groupSub.getTittle())));
    }

    @Test
    void shouldProperlyExecuteUnsubscribeCommandWithNonExistingGroupSub() {
        //given
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(1234L);
        when(message.getText()).thenReturn("/unsubscribe 100");
        when(groupSubService.findById(100L)).thenReturn(Optional.empty());

        //when
        unsubscribeCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(eq("1234"), eq(String.format(UnsubscribeCommand.NOT_FOUND, 100)));
        verifyNoMoreInteractions(groupSubService);
    }

    @Test
    void shouldProperlySendMessageIfGroupIdInvalid() {
        //given
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(1234L);
        when(message.getText()).thenReturn("/unsubscribe foo");

        //when
        unsubscribeCommand.execute(update);

        //then
        verify(sendBotMessageService).sendMessage(eq("1234"), eq(UnsubscribeCommand.ID_IS_INVALID));
    }
}
