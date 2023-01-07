package com.animalshelter.animalshelterbot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Класс отвечающий за отправку фото {@link #telegramSendPhoto(SendPhoto)}.
 */
@Service
@RequiredArgsConstructor
public class TelegramBotSender {
    private final TelegramBot telegramBot;

    public void telegramSendPhoto (SendPhoto image){
        telegramBot.execute(image);
    }
}
