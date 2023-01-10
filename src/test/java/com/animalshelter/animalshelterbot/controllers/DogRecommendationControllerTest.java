package com.animalshelter.animalshelterbot.controllers;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class DogRecommendationControllerTest {

    @InjectMocks
    DogRecommendationController dogRecommendationController;

    @Test
    public void handleMeetingRulesCallbackMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogRecommendationControllerTest.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callback = getCallback(json);

        SendMessage message = dogRecommendationController.handleMeetingRulesCallbackMessage(callback);
        assertThat(message.getParameters().get("chat_id")).isEqualTo(callback.from().id());
        assertThat(message.getParameters().get("text")).isNotNull();
    }

    private CallbackQuery getCallback(String json) {
        return BotUtils.fromJson(json, CallbackQuery.class);
    }
}