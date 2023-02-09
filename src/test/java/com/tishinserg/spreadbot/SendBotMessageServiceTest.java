package com.tishinserg.spreadbot;


import com.tishinserg.spreadbot.bot.SpreadBotTelegram;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.SendBotMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@DisplayName("Unit-level testing for SendBotMessageService")
public class SendBotMessageServiceTest {

    private SendBotMessageService sendBotMessageService;
    private SpreadBotTelegram spreadBotTelegram;

    @BeforeEach
    public void init() {
        spreadBotTelegram = Mockito.mock(SpreadBotTelegram.class);
        sendBotMessageService = new SendBotMessageServiceImpl(spreadBotTelegram);
    }

    @Test
    public void shouldProperlySendMessage() throws TelegramApiException {
        //given
        String chatId = "test_chat_id";
        String message = "test_message";

        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.enableHtml(true);

        //when
        sendBotMessageService.sendMessage(chatId, message);

        //then
        Mockito.verify(spreadBotTelegram).execute(sendMessage);


    }
}
