package com.tishinserg.spreadbot.command;

import org.junit.jupiter.api.DisplayName;

import static com.tishinserg.spreadbot.command.CommandName.STAT;


@DisplayName("Unit-level testing for StatCommandTest")
public class StatCommandTest extends AbstractCommandTest{
    @Override
    String getCommandName() {
        return STAT.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return String.format(StatCommand.STAT_MESSAGE, 0);
    }

    @Override
    Command getCommand() {
        return new StatCommand(sendBotMessageService, telegramUserService);
    }
}
