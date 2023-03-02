package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.command.Command;
import com.tishinserg.spreadbot.command.CommandContainer;
import com.tishinserg.spreadbot.command.StatCommand;
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
        return StatCommand.STAT_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StatCommand(sendBotMessageService, telegramUserService);
    }
}
