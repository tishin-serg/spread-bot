package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.tishinserg.spreadbot.command.CommandName.*;

public class HelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨<b>Доcтупные команды</b>✨\n\n"

                    + "<b>Начать\\закончить работу с ботом</b>\n"
                    + "%s - начать работу со мной\n"
                    + "%s - приостановить работу со мной\n\n"
                    + "%s - получить помощь в работе со мной\n"
                    + "%s - получить статистику по количеству пользователей\n"
                    + "%s - получить текущий курс юнистрим",
            START.getCommandName(), STOP.getCommandName(), HELP.getCommandName(), STAT.getCommandName(), UNI.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }


    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}
