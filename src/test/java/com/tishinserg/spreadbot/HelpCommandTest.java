package com.tishinserg.spreadbot;


import com.tishinserg.spreadbot.command.Command;
import com.tishinserg.spreadbot.command.HelpCommand;
import org.junit.jupiter.api.DisplayName;

import static com.tishinserg.spreadbot.command.CommandName.HELP;
import static com.tishinserg.spreadbot.command.HelpCommand.HELP_MESSAGE;

@DisplayName("Unit-level testing for HelpCommand")
public class HelpCommandTest extends AbstractCommandTest {


    @Override
    String getCommandName() {
        return HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new HelpCommand(sendBotMessageService);
    }
}
