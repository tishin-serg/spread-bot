package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.service.GroupSubService;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnsubscribeCommand implements Command {
    public final static String UN_SUB_MESSAGE = "Вы отписаны от курса <b>%s</b>";
    public final static String NOT_FOUND = "Группа подписки id%s не найдена";
    public static final String ID_IS_INVALID = "Неправильный формат ID группы. Он должен являться числом. Например /unbscribe 1";
    private final SendBotMessageService sendBotMessageService;
    private final GroupSubService groupSubService;
    private final CommandContainer commandContainer;

    @Autowired
    public UnsubscribeCommand(SendBotMessageService sendBotMessageService, GroupSubService groupSubService, CommandContainer commandContainer) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupSubService = groupSubService;
        this.commandContainer = commandContainer;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        //todo Написать тест под этот вариант
        if (update.getMessage().getText().equals(CommandName.UN_SUB.getCommandName())) {
            sendBotMessageService.sendMessage(chatId,
                    "От какой группы вы хотите отписаться? Список подписок в следующем сообщении");
            commandContainer.retrieveCommand(CommandName.SUBSCRIPTIONS.getCommandName()).execute(update);
            return;
        }
        String s = update.getMessage().getText().split(" ")[1];
        if (!NumberUtils.isParsable(s)) {
            sendIdIsNotNumber(chatId);
            return;
        }
        Long groupId = Long.valueOf(s);
        if (groupSubService.findById(groupId).isEmpty()) {
            sendBotMessageService.sendMessage(chatId, String.format(NOT_FOUND, groupId));
            return;
        }
        GroupSub groupSub = groupSubService.unsubscribe(chatId, groupId);
        String subscribeName = groupSub.getTittle();
        sendBotMessageService.sendMessage(chatId, String.format(UN_SUB_MESSAGE, subscribeName));
    }

    private void sendIdIsNotNumber(String chatId) {
        sendBotMessageService.sendMessage(chatId, ID_IS_INVALID);
    }
}
