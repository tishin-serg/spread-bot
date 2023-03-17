package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.bot.SpreadBotTelegram;
import com.tishinserg.spreadbot.command.Command;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.SendBotMessageServiceImpl;
import com.tishinserg.spreadbot.service.TelegramUserService;
import com.tishinserg.spreadbot.service.UnistreamRateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class AbstractCommandTest {
    protected SpreadBotTelegram spreadBotTelegram = Mockito.mock(SpreadBotTelegram.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(spreadBotTelegram);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
    protected UnistreamRateService unistreamRateService = Mockito.mock(UnistreamRateService.class);

    abstract String getCommandName();
    abstract String getCommandMessage();
    abstract Command getCommand();

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given
        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);

        //todo корректно ли тут мокать сущность
        UnistreamRate unistreamRate = Mockito.mock(UnistreamRate.class);
        Mockito.when(unistreamRate.getId()).thenReturn(1L);
        Mockito.when(unistreamRate.getCurrency()).thenReturn("RUB");
        Mockito.when(unistreamRate.getDate()).thenReturn(LocalDateTime.now());
        Mockito.when(unistreamRate.getRate()).thenReturn(1.0);
        Mockito.when(unistreamRateService.findLastRate()).thenReturn(unistreamRate);

        //when
        getCommand().execute(update);

        //then
        Mockito.verify(spreadBotTelegram).execute(sendMessage);
    }
}
