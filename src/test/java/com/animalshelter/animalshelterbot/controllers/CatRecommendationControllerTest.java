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
class CatRecommendationControllerTest {

    @InjectMocks
    CatRecommendationController catRecommendationController;

    @Mock
    TelegramBotSender telegramBotSender;

    @Test
    public void handleMeetingRulesCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatRecommendationController.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = catRecommendationController.handleMeetingRulesCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleCatDeclineCausesCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = catRecommendationController.handleCatDeclineCausesCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleCatDocListCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = catRecommendationController.handleCatDocListCallbackMessage(callback);
        Mockito.verify(telegramBotSender).sendDocument(any(SendDocument.class));

        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleCatHousingRecommendationCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = catRecommendationController.handleCatHousingRecommendationCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleKittyHousingRecommendationCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = catRecommendationController.handleKittyHousingRecommendationCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleCatTransportationRecommendationCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = catRecommendationController.handleCatTransportationRecommendationCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    @Test
    public void handleCatDisabledHousingCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = catRecommendationController.handleCatDisabledHousingCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    private CallbackQuery getCallback(String json) {
        return BotUtils.fromJson(json, CallbackQuery.class);
    }
}