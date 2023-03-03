package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.command.Command;
import com.tishinserg.spreadbot.command.StopCommand;
import org.junit.jupiter.api.DisplayName;

import static com.tishinserg.spreadbot.command.CommandName.STOP;
import static com.tishinserg.spreadbot.command.StopCommand.STOP_MESSAGE;

@DisplayName("Unit-level testing for StopCommandTest")
public class StopCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return STOP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return STOP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StopCommand(sendBotMessageService, telegramUserService);
    }
}
