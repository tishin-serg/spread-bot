package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.command.Command;
import com.tishinserg.spreadbot.command.NoCommand;
import org.junit.jupiter.api.DisplayName;

import static com.tishinserg.spreadbot.command.CommandName.NO;
import static com.tishinserg.spreadbot.command.NoCommand.NO_MESSAGE;

@DisplayName("Unit-level testing for NoCommand")
public class NoCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return NO.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return NO_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new NoCommand(sendBotMessageService);
    }
}
