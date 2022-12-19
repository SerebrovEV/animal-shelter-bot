package com.animalshelter.animalshelterbot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final TelegramBot bot;

    private final String startText = "Приветственное сообщение бота";


    public void handle(Message message) {
        if(message.text() == null) {
            return;
        }

        if(message.text().equals("/start")) {
            this.handleStartMessage(message);
        }
    }

    private void handleStartMessage(Message message) {
        SendMessage answer = new SendMessage(message.chat().id(), this.startText);
        SendResponse response = bot.execute(answer);
    }
}
