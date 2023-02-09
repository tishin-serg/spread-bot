package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.service.SendBotMessageService;

import java.util.Map;

import static com.tishinserg.spreadbot.command.CommandName.*;


public class CommandContainer {
    private final Map<String, Command> commandMap;
    private final UnknownCommand unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService) {
        commandMap = Map.ofEntries(
                Map.entry(START.getCommandName(), new StartCommand(sendBotMessageService)),
                Map.entry(STOP.getCommandName(), new StopCommand(sendBotMessageService)),
                Map.entry(HELP.getCommandName(), new HelpCommand(sendBotMessageService)),
                Map.entry(NO.getCommandName(), new NoCommand(sendBotMessageService)));

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandId) {
        return commandMap.getOrDefault(commandId, unknownCommand);
    }
}
