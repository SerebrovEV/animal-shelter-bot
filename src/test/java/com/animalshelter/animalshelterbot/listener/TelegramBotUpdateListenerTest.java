package com.animalshelter.animalshelterbot.listener;

import com.animalshelter.animalshelterbot.handler.MessageHandler;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdateListenerTest {

    @InjectMocks
    TelegramBotUpdateListener telegramBotUpdateListener;

    @Mock
    MessageHandler messageHandler;

    @Mock
    TelegramBot telegramBot;

    @Spy
    InvocationTargetException exception;

    @Test
    void init() {
        telegramBotUpdateListener.init();
        verify(telegramBot).setUpdatesListener(any(UpdatesListener.class));
    }

    @Test
    void processMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdateListener.class.getResource("update_message.json").toURI()));
        Update update = getUpdate(json);
        List<Update> updates = List.of(update);
        telegramBotUpdateListener.process(updates);
        verify(messageHandler).handleMessage(any(Message.class));
    }

    @Test
    void processCallback() throws URISyntaxException, IOException, InvocationTargetException, IllegalAccessException {
        String json = Files.readString(Paths.get(TelegramBotUpdateListener.class.getResource("update_callback.json").toURI()));
        Update update = getUpdate(json);
        List<Update> updates = List.of(update);
        telegramBotUpdateListener.process(updates);
        verify(messageHandler).handleCallback(any(CallbackQuery.class));
    }

    @Test
    void processCallbackException() throws URISyntaxException, IOException, InvocationTargetException, IllegalAccessException {
        String json = Files.readString(Paths.get(TelegramBotUpdateListener.class.getResource("update_callback.json").toURI()));
        Update update = getUpdate(json);
        List<Update> updates = List.of(update);
        doThrow(exception).when(messageHandler).handleCallback(any(CallbackQuery.class));
        doNothing().when(exception).printStackTrace();
        telegramBotUpdateListener.process(updates);
        verify(exception).printStackTrace();
    }


    private Update getUpdate(String json) {
        return BotUtils.fromJson(json, Update.class);
    }

}