package com.tishinserg.spreadbot.bot;

import com.tishinserg.spreadbot.command.CommandContainer;
import com.tishinserg.spreadbot.service.SendBotMessageServiceImpl;
import com.tishinserg.spreadbot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.tishinserg.spreadbot.command.CommandName.NO;

@Component
public class SpreadBotTelegram extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    @Autowired
    public SpreadBotTelegram(TelegramUserService telegramUserService) {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), telegramUserService);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();

            if (message.startsWith(COMMAND_PREFIX)) {
                String commandId = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandId).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }


        }
    }
}
