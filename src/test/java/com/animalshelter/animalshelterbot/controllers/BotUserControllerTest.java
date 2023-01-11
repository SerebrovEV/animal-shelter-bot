package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.service.ValidatorUserService;
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
    private ValidatorUserService validatorUserService;

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи отправьте сообщение в форме:\n 89871234567 Иван \n и мы вам перезвоним.";

    @BeforeEach
    public void setOut() {
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
    }

    @Test
    void addMessage() {
        SendMessage expected = new SendMessage(1L, ADD_MESSAGE);
        SendMessage actual = out.addMessage(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

//    @Test
//    void getContactMessage() {
//        BotUser botUser = new BotUser("Test", 89871234567L, 1L);
//        when(validatorUserService.validateGetUser(message)).thenReturn(botUser.toStringUser());
//        SendMessage expected = new SendMessage(1L,botUser.toStringUser());
//
//        SendMessage actual = out.getContactMessage(message);
//        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
//        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
//    }

    @Test
    void addBotUser() {
        BotUser botUser = new BotUser("Test", 89871234567L, 1L);
        SendMessage expected = new SendMessage(1L,botUser.toStringUser());
        when(validatorUserService.validateUser(any())).thenReturn(botUser.toStringUser());

        SendMessage actual = out.addBotUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }
}