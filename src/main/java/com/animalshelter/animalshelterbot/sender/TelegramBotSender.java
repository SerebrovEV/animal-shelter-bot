package com.animalshelter.animalshelterbot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Класс отвечающий за отправку фото {@link #telegramSendPhoto(SendPhoto)}, документов {@link #sendDocument(SendDocument)}
 * или сообщений {@link #sendMessage(SendMessage)}.
 */
@Service
@RequiredArgsConstructor
public class TelegramBotSender {
    private final TelegramBot telegramBot;

    public void telegramSendPhoto (SendPhoto image){
        telegramBot.execute(image);
    }

    public void sendDocument(SendDocument document) {
        telegramBot.execute(document);
    }

    public void sendMessage(SendMessage message) {
        telegramBot.execute(message);
    }
}
