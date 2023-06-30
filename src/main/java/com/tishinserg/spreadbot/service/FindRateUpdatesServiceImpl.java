package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FindRateUpdatesServiceImpl implements FindRateUpdatesService {

    private final GroupSubService groupSubService;
    private final SendBotMessageService sendBotMessageService;
    private final RateServiceFactory rateServiceFactory;

    @Autowired
    public FindRateUpdatesServiceImpl(GroupSubService groupSubService,
                                      SendBotMessageService sendBotMessageService,
                                      RateServiceFactory rateServiceFactory) {
        this.groupSubService = groupSubService;
        this.sendBotMessageService = sendBotMessageService;
        this.rateServiceFactory = rateServiceFactory;
    }

    @Override
    public void findRateUpdates() {
        List<GroupSub> groupSubs = groupSubService.findAllGroups().stream()
                .sorted(Comparator.comparing(GroupSub::getId))
                .collect(Collectors.toList());
        ExecutorService executorService = Executors.newFixedThreadPool(groupSubs.size());
        groupSubs.forEach(groupSub -> executorService.submit(() -> {
            compareRatesAndNotify(groupSub);
        }));
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // обработка исключения
        }
    }

    private void sendFormattedMessage(GroupSub groupSub, Rate currentRate, Boolean isHigher) {
        String higherEmoji = "\uD83D\uDD3C";
        String lowerEmoji = "\uD83D\uDD3D";
        String message = "%s\nТекущий курс: <b>%s руб.</b> %s\nВремя: %s";
        LocalDateTime localDateTime = currentRate.getDate();
        String date = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
        groupSub.getUsers().stream().filter(TelegramUser::isActive).forEach(telegramUser -> {
            if (isHigher) {
                sendBotMessageService.sendMessage(telegramUser.getChatId(),
                        String.format(message, groupSub.getTittle(), currentRate.getRate(), higherEmoji, date));
            } else {
                sendBotMessageService.sendMessage(telegramUser.getChatId(),
                        String.format(message, groupSub.getTittle(), currentRate.getRate(), lowerEmoji, date));
            }
        });
    }

    public void compareRatesAndNotify(GroupSub groupSub) {
        RateService rateService = rateServiceFactory.createInstance(groupSub.getService());
        // if (groupSub.getService().equals("unistream")) return;
        rateService.getCurrentRate(groupSub).thenAccept(rate -> {
            Optional<Rate> lastRate = rateService.getLastRate(groupSub);
            if (lastRate.isPresent() && rateService.compareRates(lastRate.get(), rate)) {
                sendFormattedMessage(groupSub, rate, rate.getRate().compareTo(lastRate.get().getRate()) > 0);
                groupSub.setLastRate(rate.getRate());
                groupSubService.save(groupSub);
            }
            if (groupSub.getLastRate() == null) {
                groupSub.setLastRate(rate.getRate());
                groupSubService.save(groupSub);
            }
            rateService.save(rate, groupSub);
        });
    }
}
