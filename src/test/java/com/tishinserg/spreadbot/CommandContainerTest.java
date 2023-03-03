package com.tishinserg.spreadbot;


import com.tishinserg.spreadbot.command.Command;
import com.tishinserg.spreadbot.command.CommandContainer;
import com.tishinserg.spreadbot.command.CommandName;
import com.tishinserg.spreadbot.command.UnknownCommand;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.TelegramUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

@DisplayName("Unit-level testing for CommandContainer")
class CommandContainerTest {


    private CommandContainer commandContainer;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        commandContainer = new CommandContainer(sendBotMessageService, telegramUserService);
    }

    @Test
    public void shouldGetAllTheExistingCommands() {
        Arrays.stream(CommandName.values())
                .forEach(commandName -> {
                    Command command = commandContainer.retrieveCommand(commandName.getCommandName());
                    Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }

    @Test
    public void shouldReturnUnknownCommand() {
        //given
        String unknownCommand = "/akjdnsk";
        //when
        Command command = commandContainer.retrieveCommand(unknownCommand);
        //then
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }

}
