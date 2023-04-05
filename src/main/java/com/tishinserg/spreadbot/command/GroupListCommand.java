package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class GroupListCommand implements Command {

    public static final String SUBSCRIPTIONS = "Я нашёл все подписки на курсы:\n%s";
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public GroupListCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        if (mapGroupList(chatId).isEmpty()) {
            sendBotMessageService.sendMessage(chatId, "Cписок подписок пуст :(");
            return;
        }
        sendBotMessageService.sendMessage(chatId, String.format(SUBSCRIPTIONS, mapGroupList(chatId)));
    }

    public List<GroupSub> findGroupSubsForUser(String chatId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        return telegramUser.getGroupSubs();
    }


    public String mapGroupList(String chatId) {
        List<GroupSub> groupSubs = findGroupSubsForUser(chatId);
        return groupSubs.stream()
                .map(groupSub -> String.format("%s - %s\n",
                        groupSub.getTittle(),
                        groupSub.getId()))
                .collect(Collectors.joining());
    }
}
