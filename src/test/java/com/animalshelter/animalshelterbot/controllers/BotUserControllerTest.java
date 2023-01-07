package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.service.BotUserService;
import com.animalshelter.animalshelterbot.service.ValidatorBotUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Тесты для проверки работоспособности {@link BotUserController}
 */
@ExtendWith(MockitoExtension.class)
class BotUserControllerTest {

    @InjectMocks
    private BotUserController out;

    @Mock
    Message message;

    @Mock
    User user;
    @Mock
    private ValidatorBotUserService validatorBotUserService;

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи введите информацию в форме:\n 89871234567 Иван \n и мы вам перезвоним.";

    @BeforeEach
    private void setOut() {
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
  //      when(validatorBotUserService.validateGetUser(any())).thenReturn()
    }

    @Test
    void addMessage() {
        SendMessage expected = new SendMessage(1L, ADD_MESSAGE);
        SendMessage actual = out.addMessage(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));

    }

    @Test
    void getContactMessage() {
    }

    @Test
    void addBotUser() {
    }
}