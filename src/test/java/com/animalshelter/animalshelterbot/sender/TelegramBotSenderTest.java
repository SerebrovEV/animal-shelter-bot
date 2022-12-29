package com.animalshelter.animalshelterbot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendPhoto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;


class TelegramBotSenderTest {
    private TelegramBotSender telegramBotSender;
    private TelegramBot telegramBot;

    @BeforeEach
    public void setUp() {
        telegramBot = Mockito.mock(TelegramBot.class);
        telegramBotSender = new TelegramBotSender(telegramBot);
        }

    @Test
    public void shouldSendPhoto() {
        String chatId = "test_chat_id";
        File image = new File("src/main/resources/images/scheme.PNG");
        SendPhoto sendPhoto = new SendPhoto(chatId, image);
        telegramBotSender.telegramSendPhoto(sendPhoto);
        Mockito.verify(telegramBot).execute(sendPhoto);
    }
}