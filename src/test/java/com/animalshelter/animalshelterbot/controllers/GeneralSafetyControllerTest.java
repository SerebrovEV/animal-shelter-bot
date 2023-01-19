package com.animalshelter.animalshelterbot.controllers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralSafetyControllerTest {

    @InjectMocks
    GeneralSafetyController generalSafetyController;

    @Mock
    User user;

    @Mock
    CallbackQuery callbackQuery;

    private final String generalSafetyInfoText =
            "Рекомендации по технике безопасности:\n"
                    + "    - удобная обувь с нескользящей подошвой;\n"
                    + "    - одежда закрытого типа;\n"
                    + "    - за ограждения не заходить;\n"
                    + "    - животных с рук не кормить;\n"
                    + "    - в вольеры c животными руки не совать.";


    @Test
    public void handleCallbackSafetyDogRules() {

        SendMessage expected = new SendMessage(1L, generalSafetyInfoText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = generalSafetyController.handleCallbackSafetyDogRules(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(expected.getParameters().get("chat_id"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void handleCallbackSafetyCatRules() {

        SendMessage expected = new SendMessage(1L, generalSafetyInfoText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = generalSafetyController.handleCallbackSafetyCatRules(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(expected.getParameters().get("chat_id"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

}