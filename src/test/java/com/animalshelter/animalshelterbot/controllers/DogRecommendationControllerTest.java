package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DogRecommendationControllerTest {

    @InjectMocks
    DogRecommendationController dogRecommendationController;

    @Mock
    TelegramBotSender telegramBotSender;

    @Test
    public void handleMeetingRulesCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = dogRecommendationController.handleMeetingRulesCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleCynologistsRecommendationCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = dogRecommendationController.handleCynologistsRecommendationCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleDogDeclineCausesCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = dogRecommendationController.handleDogDeclineCausesCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleDogDocListCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = dogRecommendationController.handleDogDocListCallbackMessage(callback);
        Mockito.verify(telegramBotSender).sendDocument(any(SendDocument.class));

        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleCytologistAdviceCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = dogRecommendationController.handleCytologistAdviceCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleDogDisabledHousingCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = dogRecommendationController.handleDogDisabledHousingCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }


    private CallbackQuery getCallback(String json) {
        return BotUtils.fromJson(json, CallbackQuery.class);
    }
}