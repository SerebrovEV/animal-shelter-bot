package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.impl.ValidateDogUserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Тесты для проверки работоспособности {@link DogUserController}
 */
@ExtendWith(MockitoExtension.class)
class DogUserControllerTest {

    @InjectMocks
    private DogUserController out;

    @Mock
    Message message;

    @Mock
    CallbackQuery callbackQuery;

    @Mock
    User user;

    @Mock
    private ValidateDogUserService validateDogUserService;

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи отправьте сообщение в форме:\n Возьму собаку 89871234567 Иван \n и мы вам перезвоним.";

    @Test
    void handleAddMessageDog() {
        SendMessage expected = new SendMessage(1L, ADD_MESSAGE);
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = out.handleAddMessageDog(callbackQuery);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));

    }

    @Test
    void handleAddDogUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L, 1L);
        SendMessage expected = new SendMessage(1L, dogUser.toStringUser());
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        when(validateDogUserService.validateUser(any())).thenReturn(dogUser.toStringUser());

        SendMessage actual = out.handleAddDogUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }
    @Test
    void handleAddCatUserIDChat() {
        DogUser dogUser = new DogUser("Test", 89871234567L, 1L);
        SendMessage expected = new SendMessage(1L, dogUser.toStringUser());
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        when(validateDogUserService.validateUserIdChat(any())).thenReturn(dogUser.toStringUser());

        SendMessage actual = out.handleAddDogUserIDChat(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }
}