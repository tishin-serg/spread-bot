package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Statistics {@link Command}.
 */
public class StatCommand implements Command {

    public final static String STAT_MESSAGE = "Spread Bot использует %s человек";
    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
        this.sendBotMessageService = sendBotMessageService;
    }


    @Override
    public void execute(Update update) {
        int activeUserCount = telegramUserService.retrieveAllActiveUsers().size();
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), String.format(STAT_MESSAGE, activeUserCount));
    }
}
