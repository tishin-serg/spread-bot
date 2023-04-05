package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.service.GroupSubService;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.TelegramUserService;
import com.tishinserg.spreadbot.service.UnistreamRateService;

import java.util.Map;

import static com.tishinserg.spreadbot.command.CommandName.*;

public class CommandContainer {
    private final Map<String, Command> commandMap;
    private final UnknownCommand unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            TelegramUserService telegramUserService,
                            UnistreamRateService unistreamRateService,
                            GroupSubService groupSubService) {
        commandMap = Map.ofEntries(
                Map.entry(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService)),
                Map.entry(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService)),
                Map.entry(HELP.getCommandName(), new HelpCommand(sendBotMessageService)),
                Map.entry(NO.getCommandName(), new NoCommand(sendBotMessageService)),
                Map.entry(STAT.getCommandName(), new StatCommand(sendBotMessageService, telegramUserService)),
                Map.entry(UNI.getCommandName(), new UniCommand(sendBotMessageService, unistreamRateService)),
                Map.entry(SUB.getCommandName(), new SubscribeCommand(sendBotMessageService, groupSubService, telegramUserService)),
                Map.entry(UN_SUB.getCommandName(), new UnsubscribeCommand(sendBotMessageService, groupSubService, this)),
                Map.entry(SUBSCRIPTIONS.getCommandName(), new GroupListCommand(sendBotMessageService, telegramUserService)));

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandId) {
        return commandMap.getOrDefault(commandId, unknownCommand);
    }
}
