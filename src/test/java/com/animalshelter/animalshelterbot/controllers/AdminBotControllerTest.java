package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.BotUser;
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
import static org.junit.jupiter.api.Assertions.*;
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
        BotUser botUser = new BotUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, botUser.toString());
        when(validatorUserService.validateUserFromAdmin(message)).thenReturn(botUser.toString());
        SendMessage actual = out.createBotUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void getBotUser() {
        BotUser botUser = new BotUser("Test", 89871234567L);
        when(validatorUserService.validateGetUserFromAdmin(any())).thenReturn(botUser.toString());

        SendMessage expected = new SendMessage(1L, botUser.toString());

        SendMessage actual = out.getBotUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void deleteBotUser() {
        BotUser botUser = new BotUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, botUser.toString());
        when(validatorUserService.validateDeleteUser(any())).thenReturn(botUser.toString());

        SendMessage actual = out.deleteBotUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void editBotUser() {
        BotUser botUser = new BotUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, botUser.toString());
        when(validatorUserService.validateEditUser(message)).thenReturn(botUser.toString());

        SendMessage actual = out.editBotUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void getAllBotUser() {
        BotUser botUser = new BotUser("Test", 89871234567L);
        BotUser botUser2 = new BotUser("Test2", 89871234568L);
        BotUser botUser3 = new BotUser("Test3", 89871234569L);
        BotUser botUser4 = new BotUser("Test4", 89871234561L);
        List<BotUser> botUsers = List.of(botUser, botUser2, botUser3, botUser4);
        when(botUserService.getAll()).thenReturn(botUsers);

        SendMessage expected = new SendMessage(1L, List.of(botUser, botUser2, botUser3, botUser4).toString());
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