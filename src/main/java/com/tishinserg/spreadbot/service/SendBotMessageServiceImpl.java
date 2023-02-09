package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.bot.SpreadBotTelegram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final SpreadBotTelegram spreadBotTelegram;

    @Autowired
    public SendBotMessageServiceImpl(SpreadBotTelegram spreadBotTelegram) {
        this.spreadBotTelegram = spreadBotTelegram;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.enableHtml(true);
        try {
            spreadBotTelegram.execute(sendMessage);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }
}
