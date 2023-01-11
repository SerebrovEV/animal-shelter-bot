package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.BotUserService;
import com.animalshelter.animalshelterbot.service.ValidatorUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import liquibase.pro.packaged.B;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminBotControllerTest {

    @InjectMocks
    AdminBotController out;
    @Mock
    ValidatorUserService validatorUserService;
    @Mock
    Message message;
    @Mock
    User user;

    @Mock
    BotUserService botUserService;

    private final String ADMIN_COMMAND = "Правила использования: \n" +
            "Сохранить 89871234567 Иван - добавление контакта усыновителя;\n" +
            "Найти 10 - поиск пользователя с id = 10;\n" +
            "Изменить 10 89871234567 Миша - изменение пользователя с id = 10;\n" +
            "Удалить 10 - удаление пользователя с id = 10;\n" +
            "/getAll - получить список всех усыновителей;\n" +
            "/badUser - получить список усыновителей, которые не прислали отчеты;\n" +
            "Отчет 10 - получить последний отчет от усыновителя id = 10.";

    @BeforeEach
    public void setOut() {
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
    }

    @Test
    void infoAboutUseBot() {
        SendMessage expected = new SendMessage(1L, ADMIN_COMMAND);
        SendMessage actual = out.infoAboutUseBot(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void createBotUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, dogUser.toString());
        when(validatorUserService.validateUserFromAdmin(message)).thenReturn(dogUser.toString());
        SendMessage actual = out.createBotUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void getBotUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        when(validatorUserService.validateGetUserFromAdmin(any())).thenReturn(dogUser.toString());

        SendMessage expected = new SendMessage(1L, dogUser.toString());

        SendMessage actual = out.getBotUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void deleteBotUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, dogUser.toString());
        when(validatorUserService.validateDeleteUser(any())).thenReturn(dogUser.toString());

        SendMessage actual = out.deleteBotUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void editBotUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, dogUser.toString());
        when(validatorUserService.validateEditUser(message)).thenReturn(dogUser.toString());

        SendMessage actual = out.editBotUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void getAllBotUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        DogUser dogUser2 = new DogUser("Test2", 89871234568L);
        DogUser dogUser3 = new DogUser("Test3", 89871234569L);
        DogUser dogUser4 = new DogUser("Test4", 89871234561L);
        List<DogUser> dogUsers = List.of(dogUser, dogUser2, dogUser3, dogUser4);
        when(botUserService.getAll()).thenReturn(dogUsers);

        SendMessage expected = new SendMessage(1L, List.of(dogUser, dogUser2, dogUser3, dogUser4).toString());
        SendMessage actual = out.getAllBotUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));

    }

//    @Test
//    void getBadUser() {
//    }

//    @Test
//    void getLastReport() {
//    }
}