package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FindRateUpdatesServiceImpl implements FindRateUpdatesService {

    private final GroupSubService groupSubService;
    private final SendBotMessageService sendBotMessageService;
    private final UnistreamRateService unistreamRateService;

    @Autowired
    public FindRateUpdatesServiceImpl(GroupSubService groupSubService, SendBotMessageService sendBotMessageService, UnistreamRateService unistreamRateService) {
        this.groupSubService = groupSubService;
        this.sendBotMessageService = sendBotMessageService;
        this.unistreamRateService = unistreamRateService;
    }

    //todo Оптимизировать сохранение курсов в БД пачкой
    @Override
    public void findRateUpdates() {
        groupSubService.findAllGroups().forEach(groupSub -> {
            UnistreamRate currentUnistreamRate = unistreamRateService.getCurrentRate(
                    groupSub.getCountry(),
                    groupSub.getCurrencyFrom(),
                    groupSub.getCurrencyTo());
            UnistreamRate lastUnistreamRate = unistreamRateService.getLastRate(groupSub.getCountry(),
                    groupSub.getCurrencyFrom());
            if (groupSub.getLastRate() == null) {
                groupSub.setLastRate(currentUnistreamRate.getRate());
                groupSubService.save(groupSub);
            }
            if (unistreamRateService.compareUnistreamRates(lastUnistreamRate, currentUnistreamRate)) {
                notifySubscribersAboutRateUpdate(groupSub, currentUnistreamRate);
                groupSub.setLastRate(currentUnistreamRate.getRate());
                groupSubService.save(groupSub);
            }
            unistreamRateService.save(currentUnistreamRate);
        });
    }

    //todo нужно ли тестировать приватные методы?
    private void notifySubscribersAboutRateUpdate(GroupSub groupSub, UnistreamRate currentUnistreamRate) {
        String message = "%s\nТекущий курс: <b>%s руб.</b>\nВремя: %s";
        LocalDateTime localDateTime = currentUnistreamRate.getDate();
        String date = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
        groupSub.getUsers().stream().filter(TelegramUser::isActive).forEach(telegramUser -> {
            sendBotMessageService.sendMessage(telegramUser.getChatId(),
                    String.format(message, groupSub.getTittle(), currentUnistreamRate.getRate(), date));
        });
    }
}
