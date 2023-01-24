package com.animalshelter.animalshelterbot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
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

    @Test
    public void shouldSendDocument() {
        String chatId = "test_chat_id";
        File image = new File("src/main/resources/images/scheme.PNG");
        SendDocument sendDocument = new SendDocument(chatId, image);
        telegramBotSender.sendDocument(sendDocument);
        Mockito.verify(telegramBot).execute(sendDocument);
    }

    @Test
    public void shouldSendMessage() {
        String chatId = "test_chat_id";
        SendMessage sendMessage = new SendMessage(chatId, "test");
        telegramBotSender.sendMessage(sendMessage);
        Mockito.verify(telegramBot).execute(sendMessage);
    }
}