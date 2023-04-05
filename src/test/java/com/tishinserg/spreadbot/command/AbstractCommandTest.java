package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.bot.SpreadBotTelegram;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import com.tishinserg.spreadbot.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

abstract class AbstractCommandTest {
    protected SpreadBotTelegram spreadBotTelegram = Mockito.mock(SpreadBotTelegram.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(spreadBotTelegram);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
    protected UnistreamRateService unistreamRateService = Mockito.mock(UnistreamRateService.class);
    protected GroupSubService groupSubService = Mockito.mock(GroupSubService.class);

    public static Update prepareUpdate(Long chatId, String commandName) {
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(commandName);
        update.setMessage(message);
        return update;
    }

    abstract String getCommandName();

    abstract String getCommandMessage();

    abstract Command getCommand();

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given
        Long chatId = 1234567824356L;

        Update update = prepareUpdate(chatId, getCommandName());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);


        prepareUnistreamRate();

        //when
        getCommand().execute(update);

        //then
        Mockito.verify(spreadBotTelegram).execute(sendMessage);
    }

    private UnistreamRate prepareUnistreamRate() {
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setId(1L);
        unistreamRate.setCountry("GEO");
        unistreamRate.setDate(LocalDateTime.now());
        unistreamRate.setRate(BigDecimal.valueOf(1.0));
        unistreamRate.setCurrency("USD");
        Mockito.when(unistreamRateService.getCurrentRate("GEO", "USD", "RUB")).thenReturn(unistreamRate);
        return unistreamRate;
    }
}
