package com.animalshelter.animalshelterbot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TelegramBotSender {

    private final TelegramBot telegramBot;

    public void sendMessage(SendMessage message) {
        telegramBot.execute(message);
    }

}
