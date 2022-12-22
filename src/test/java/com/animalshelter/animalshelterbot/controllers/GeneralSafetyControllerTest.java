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
    Message message;

    @Mock
    User user;

    @Mock
    CallbackQuery callbackQuery;

    private final String generalSafetyInfoText =
            "Рекоменации по технике безопасности:\n"
                    + "    - удобная обувь с нескользящей подошвой;\n"
                    + "    - одежда закрытого типа;\n"
                    + "    - за ограждения не заходить;\n"
                    + "    - животных с рук не кормить;\n"
                    + "    - в вальеры c животными руки не сувать.";

    @Test
    public void handleSafetyMessage() {

        SendMessage expected = new SendMessage(1L, generalSafetyInfoText);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = generalSafetyController.handleSafetyMessage(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actual.getParameters().get("text")).isEqualTo(generalSafetyInfoText);
    }

    @Test
    public void handleCallbackMessage() {

        SendMessage expected = new SendMessage(1L, generalSafetyInfoText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = generalSafetyController.handleCallbackMessage(callbackQuery);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actual.getParameters().get("text")).isEqualTo(generalSafetyInfoText);
    }

}