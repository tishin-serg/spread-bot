package com.tishinserg.spreadbot.command;

import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import com.tishinserg.spreadbot.service.SendBotMessageService;
import com.tishinserg.spreadbot.service.UnistreamRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Unistream rate {@link Command}.
 */
public class UniCommand implements Command {

    public final static String UNI_MESSAGE = "Текущий курс: <b> %s руб.</b> \n Время: %s";
    private final SendBotMessageService sendBotMessageService;
    private final UnistreamRateService unistreamRateService;

    @Autowired
    public UniCommand(SendBotMessageService sendBotMessageService, UnistreamRateService unistreamRateService) {
        this.sendBotMessageService = sendBotMessageService;
        this.unistreamRateService = unistreamRateService;
    }

    @Override
    public void execute(Update update) {
        // todo переписать
//        Double rate = UnistreamConverter.jSonToEntity(unistreamRateParsingService.getRate().getFees().get(0)).getRate();
//        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), String.format(UNI_MESSAGE, rate));
        UnistreamRate rateModel = unistreamRateService.getCurrentRate("GEO", "USD", "RUB");
        LocalDateTime localDateTime = rateModel.getDate();
        String date = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
        BigDecimal rate = rateModel.getRate();
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), String.format(UNI_MESSAGE, rate, date));
    }
}
