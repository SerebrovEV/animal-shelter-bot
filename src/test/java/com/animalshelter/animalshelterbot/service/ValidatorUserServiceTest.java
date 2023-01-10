package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ValidatorUserServiceTest {
    @InjectMocks
    private ValidatorUserService out;

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

        String expected = "Добавлена запись контакта: " + botUser.toStringUser();
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
        String expected = botUser.toStringUser();
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
    @Test
    void validateUserFromAdmin(){
        BotUser botUser = new BotUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить 89871234567 Иван");
        when(botUserService.getByPhoneNumber(89871234567L)).thenReturn(null);
        when(botUserService.addBotUser(botUser)).thenReturn(botUser);

        String expected = "Добавлена запись контакта: " + botUser;
        String actual = out.validateUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserFromAdminExistingUser(){
        BotUser botUser = new BotUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить 89871234567 Иван");
        when(botUserService.getByPhoneNumber(89871234567L)).thenReturn(botUser);

        String expected = "Данный усыновитель уже есть";
        String actual = out.validateUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserFromAdminIncorrectNumber(){
        when(message.text()).thenReturn("Сохранить 79871234567 Иван");
        String expected = "Некорректный номер телефона";
        String actual = out.validateUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdmin(){
        BotUser botUser = new BotUser("Тест", 89871234567L);
        String expected = botUser.toString();
        when(message.text()).thenReturn("Найти 10");
        when(botUserService.getBotUser(10L)).thenReturn(Optional.of(botUser));
        String actual = out.validateGetUserFromAdmin(message);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdminNotFound(){
        String expected = "Усыновитель не найден, проверти правильность введения id.";
        when(message.text()).thenReturn("Найти 10");
        when(botUserService.getBotUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateGetUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdminIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateGetUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteUser(){
        BotUser botUser = new BotUser("Тест", 89871234567L);
        String expected = botUser + "удален";
        when(message.text()).thenReturn("Удалить 10");
        when(botUserService.getBotUser(10L)).thenReturn(Optional.of(botUser));
        String actual = out.validateDeleteUser(message);
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void validateDeleteUserNotFound(){
        String expected = "Усыновитель не найден, проверти правильность введения id.";
        when(message.text()).thenReturn("Удалить 10");
        when(botUserService.getBotUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateDeleteUser(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Удалить и");
        String actual = out.validateDeleteUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUser(){
        BotUser botUser = new BotUser("Тест", 89871234567L);
        BotUser botUser2 = new BotUser("Миша", 89871234562L);
        String expected = botUser2 + " изменен";
        when(message.text()).thenReturn("Изменить 10 89871234562 Миша");
        when(botUserService.getBotUser(10L)).thenReturn(Optional.of(botUser));
        String actual = out.validateEditUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectNumber(){
        String expected = "Некорректный номер телефона";
        when(message.text()).thenReturn("Изменить 10 79871234562 Миша");
        String actual = out.validateEditUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserNotFound(){
        String expected = "Усыновитель не найден, проверти правильность введения id.";
        when(message.text()).thenReturn("Изменить 10 89871234562 Миша");
        when(botUserService.getBotUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateEditUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Изменить и");
        String actual = out.validateEditUser(message);
        assertThat(actual).isEqualTo(expected);
    }



}