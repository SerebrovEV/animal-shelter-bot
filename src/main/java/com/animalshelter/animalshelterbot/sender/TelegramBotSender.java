package com.animalshelter.animalshelterbot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramBotSender {

    private final TelegramBot telegramBot;

    public void telegramSendRequest(SendPhoto image) {
        telegramBot.execute(image);
    }

    public void telegramSendRequest(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }
}
