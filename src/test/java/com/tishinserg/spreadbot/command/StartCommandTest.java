package com.tishinserg.spreadbot.command;

import org.junit.jupiter.api.DisplayName;

import static com.tishinserg.spreadbot.command.CommandName.START;
import static com.tishinserg.spreadbot.command.StartCommand.START_MESSAGE;

@DisplayName("Unit-level testing for StartCommandTest")
public class StartCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return START.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return START_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StartCommand(sendBotMessageService, telegramUserService);
    }
}
