package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ValidatorBotUserServiceTest {
    @InjectMocks
    private ValidatorBotUserService out;

    @Mock
    private BotUserService botUserService;

    @Mock
    User user;

    @Mock
    Message message;


    @Test
    void validateUser() {
        BotUser botUser = new BotUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(botUserService.getBotUserByChatId(1L)).thenReturn(null);
        when(botUserService.addBotUser(botUser)).thenReturn(botUser);

        String expected = "Добавлена запись контакта: " + botUser.toString();
        String actual = out.validateUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserIncorrectText() {
        when(message.text()).thenReturn("79871234567 Тест");
        String expected = "Некорректный номер телефона";
        String actual = out.validateUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExistingUser(){
        BotUser botUser = new BotUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(botUserService.getBotUserByChatId(1L)).thenReturn(botUser);
        String expected = "Данный пользователь уже есть";
        String actual = out.validateUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUser() {
        BotUser botUser = new BotUser("Тест", 89871234567L, 1L);
        String expected = botUser.toString();
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(botUserService.getBotUserByChatId(1L)).thenReturn(botUser);
        String actual = out.validateGetUser(message);

        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateGetUserNotFound() {
        String expected = "Клиент не найден! Пожалуйста добавьте контакты для обратной связи или" +
                " запросите вызов волонтера. Спасибо!";

        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(botUserService.getBotUserByChatId(1L)).thenReturn(null);
        String actual = out.validateGetUser(message);

        assertThat(actual).isEqualTo(expected);

    }
}