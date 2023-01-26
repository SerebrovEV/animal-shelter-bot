package com.animalshelter.animalshelterbot.handler;

import com.animalshelter.animalshelterbot.controller.CatRecommendationController;
import com.animalshelter.animalshelterbot.controller.CatReportController;
import com.animalshelter.animalshelterbot.controller.StartController;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.animalshelter.animalshelterbot.service.CatReportService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageHandlerTest {

    @InjectMocks
    MessageHandler messageHandler;

    @Mock
    TelegramBot telegramBot;

    @Mock
    TelegramBotSender telegramBotSender;

    @Mock
    CatReportService catReportService;


    @Test
    void handleMessageWithoutTextAndCaption() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(MessageHandler.class.getResource("message_without_text.json").toURI()));
        Message message = getMessage(json);
        messageHandler.handleMessage(message);
        verify(telegramBot, times(0)).execute(any(SendMessage.class));
    }

    @Test
    void handleMessageWhenCommandIsPresent() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(MessageHandler.class.getResource("message_text_start.json").toURI()));
        Message message = getMessage(json);
        CommandController controller = new StartController();
        ReflectionTestUtils.setField(messageHandler, "controllers", List.of(
                controller
        ));
        messageHandler.handleMessage(message);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void handleMessageShouldReturnListWhenCaptionIsPresentAndEqualToPattern() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(MessageHandler.class.getResource("message_caption.json").toURI()));
        Message message = getMessage(json);
        CommandController controller = new CatReportController(catReportService);
        ReflectionTestUtils.setField(messageHandler, "controllers", List.of(
                controller
        ));
        when(catReportService.getMissingReports(any(Message.class))).thenReturn(List.of(
                new SendMessage(message.from().id(), "test-01"),
                new SendMessage(message.from().id(), "test-02")
        ));
        messageHandler.handleMessage(message);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo("test-02");
    }

    @Test
    void handleCallbackWithoutText() throws URISyntaxException, IOException, InvocationTargetException, IllegalAccessException {
        String json = Files.readString(Paths.get(MessageHandler.class.getResource("callback_without_data.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        messageHandler.handleCallback(callbackQuery);
        verify(telegramBot, times(0)).execute(any(SendMessage.class));
    }

    @Test
    void handleCallbackWhenDataIsPresent() throws URISyntaxException, IOException, InvocationTargetException, IllegalAccessException {
        String json = Files.readString(Paths.get(MessageHandler.class.getResource("callback_with_data.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        CommandController controller = new CatRecommendationController(telegramBotSender);
        ReflectionTestUtils.setField(messageHandler, "controllers", List.of(
                controller
        ));
        messageHandler.handleCallback(callbackQuery);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    private Message getMessage(String json) {
        return BotUtils.fromJson(json, Message.class);
    }

    private CallbackQuery getCallback(String json) {
        return BotUtils.fromJson(json, CallbackQuery.class);
    }
}
