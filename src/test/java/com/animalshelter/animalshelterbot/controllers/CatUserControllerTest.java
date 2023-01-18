package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.ValidatorCatUserService;
import com.animalshelter.animalshelterbot.service.ValidatorDogUserService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatUserControllerTest {
    @InjectMocks
    private CatUserController out;

    @Mock
    Message message;

    @Mock
    CallbackQuery callbackQuery;

    @Mock
    User user;

    @Mock
    private ValidatorCatUserService validatorCatUserService;

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи отправьте сообщение в форме:\n Возьму кота 89871234567 Иван \n и мы вам перезвоним.";
    @Test
    void handleAddMessage() {
        SendMessage expected = new SendMessage(1L, ADD_MESSAGE);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = out.handleAddMessage(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleAddMessageCat() {
        SendMessage expected = new SendMessage(1L, ADD_MESSAGE);
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = out.handleAddMessageCat(callbackQuery);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleAddCatUser() {
        CatUser catUser = new CatUser("Test", 89871234567L, 1L);
        SendMessage expected = new SendMessage(1L, catUser.toStringUser());
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        when(validatorCatUserService.validateCatUser(any())).thenReturn(catUser.toStringUser());

        SendMessage actual = out.handleAddCatUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }
}