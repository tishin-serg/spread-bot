package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.command.Command;
import com.tishinserg.spreadbot.command.UnknownCommand;

import static com.tishinserg.spreadbot.command.UnknownCommand.UNKNOWN_MESSAGE;

public class UnknownCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return "/edsaasda";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}
