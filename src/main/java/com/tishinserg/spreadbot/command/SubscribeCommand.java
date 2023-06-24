package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import com.tishinserg.spreadbot.service.GroupSubService;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.TelegramUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubscribeCommand implements Command {

    public final static String SUB_MESSAGE = "Вы подписаны на курс <b>%s</b>\nТекущий курс: %s";
    public final static String NOT_FOUND = "Группа подписки id%s не найдена";
    public final static String LIST_GROUPS = "Чтобы подписаться на курс - передай комадну вместе с ID подписки. \n" +
            "Например: /subscribe 1. \n\n" +
            "я подготовил список всех подписок - выбирай какую хочешь :) \n\n" +
            "имя группы - ID группы \n\n" +
            "%s";
    public final static String SUB_EXIST_ALREADY = "Вы уже подписаны на курс <b> %s </b>";
    public final static String ID_IS_INVALID = "Неправильный формат ID группы. Он должен являться числом. Например /subscribe 1";
    private final SendBotMessageService sendBotMessageService;
    private final GroupSubService groupSubService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public SubscribeCommand(SendBotMessageService sendBotMessageService, GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        if (update.getMessage().getText().equalsIgnoreCase(CommandName.SUB.getCommandName())) {
            sendGroupIdList(chatId);
            return;
        }
        String s = update.getMessage().getText().split(" ")[1];
        if (!NumberUtils.isParsable(s)) {
            sendIdIsNotNumber(chatId);
            return;
        }
        Long groupId = Long.valueOf(s);
        Optional<GroupSub> optionalGroupSub = groupSubService.findById(groupId);
        if (optionalGroupSub.isEmpty()) {
            sendGroupNotFound(chatId, groupId);
            sendGroupIdList(chatId);
            return;
        }
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        if (telegramUser.getGroupSubs().contains(optionalGroupSub.get())) {
            sendSubAlreadyExist(chatId, optionalGroupSub.get());
            return;
        }
        GroupSub groupSub = groupSubService.subscribe(chatId, groupId);
        String subscribeName = groupSub.getTittle();
        String currentRate = "";
        if (groupSub.getLastRate() != null) {
            currentRate = String.format("%s руб.", groupSub.getLastRate().toString());
        }
        sendBotMessageService.sendMessage(chatId, String.format(SUB_MESSAGE, subscribeName, currentRate));
    }

    private void sendGroupNotFound(String chatId, Long groupId) {
        sendBotMessageService.sendMessage(chatId, String.format(NOT_FOUND, groupId));
    }

    private void sendGroupIdList(String chatId) {
        String groupIds = groupSubService.findAllGroups().stream()
                .sorted()
                .map(groupSub -> String.format("%s - %s \n",
                        groupSub.getTittle(),
                        groupSub.getId()))
                .collect(Collectors.joining());
        sendBotMessageService.sendMessage(chatId, String.format(LIST_GROUPS, groupIds));
    }

    private void sendSubAlreadyExist(String chatId, GroupSub groupSub) {
        String subscribeName = groupSub.getTittle();
        sendBotMessageService.sendMessage(chatId, String.format(SUB_EXIST_ALREADY, subscribeName));
    }

    private void sendIdIsNotNumber(String chatId) {
        sendBotMessageService.sendMessage(chatId, ID_IS_INVALID);
    }
}

