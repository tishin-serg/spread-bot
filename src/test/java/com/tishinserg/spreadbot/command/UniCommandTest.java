package com.tishinserg.spreadbot.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tishinserg.spreadbot.command.CommandName.UNI;

// todo тестировать эту команду отдельно
public class UniCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return UNI.getCommandName();
    }

    @Override
    String getCommandMessage() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
        return String.format(UniCommand.UNI_MESSAGE, "1.0", date);
    }

    @Override
    Command getCommand() {
        return new UniCommand(sendBotMessageService, unistreamRateService);
    }
}