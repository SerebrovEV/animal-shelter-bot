package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
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
class StartControllerTest {
    @InjectMocks
    StartController startController;

    @Mock
    Message message;

    @Mock
    User user;

    @Mock
    CallbackQuery callbackQuery;

    @Mock
    TelegramBotSender telegramBotSender;

    private static final String startMenuText = "Привет! Данный бот может помочь вам взять и содержать животное из приюта. Для продолжения выберите животное:";
    private static final String adminMenuText = "Основные команды для администратора:\n" +
            "/infoAboutAdminCat - команда для вызова меню с информацией о работе с кошками из приюта для кошек;\n" +
            "/infoAboutAdminDog - команда для вызова меню с информацией о работе с  собаками из приюта для собак;\n" +
            "/infoAboutAdminCatUser - команда для вызова меню с информацией о работе с усыновителями из приюта для кошек;\n" +
            "/infoAboutAdminDogUser - команда для вызова меню с информацией о работе с усыновителями из приюта для собак";

    private static final String dogMenuText = "Вы выбрали приют для собак. Для продолжения выберите раздел:";
    private static final String dogShelterInfoText = "В данном разделе можно получить информацию о приюте. Выберите, какую информацию вы хотите получить:";
    private static final String dogAdoptionInfoText = "В данном разделе можно получить информацию об усыновлении собаки. Выберите, какую информацию вы хотите получить:";

    private static final String catMenuText = "Вы выбрали приют для кошек. Для продолжения выберите раздел:";
    private static final String catShelterInfoText = "В данном разделе можно получить информацию о приюте. Выберите, какую информацию вы хотите получить:";
    private static final String catAdoptionInfoText = "В данном разделе можно получить информацию об усыновлении кошки. Выберите, какую информацию вы хотите получить:";

    @Test
    public void startMenuCommand() {
        SendMessage expected = new SendMessage(1L, startMenuText);

        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.startMenu(message);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void startMenu() {
        SendMessage expected = new SendMessage(1L, startMenuText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.startMenu(callbackQuery);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void dogMenu() {
        SendMessage expected = new SendMessage(1L, dogMenuText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.dogMenu(callbackQuery);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void catMenu() {
        SendMessage expected = new SendMessage(1L, catMenuText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.catMenu(callbackQuery);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void dogInfoMenu() {
        SendMessage expected = new SendMessage(1L, dogShelterInfoText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.dogInfoMenu(callbackQuery);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void catInfoMenu() {
        SendMessage expected = new SendMessage(1L, catShelterInfoText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.catInfoMenu(callbackQuery);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void dogAdoptionInfoMenu() {
        SendMessage expected = new SendMessage(1L, dogAdoptionInfoText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.dogAdoptionInfoMenu(callbackQuery);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void catAdoptionInfoMenu() {
        SendMessage expected = new SendMessage(1L, catAdoptionInfoText);

        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.catAdoptionInfoMenu(callbackQuery);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void adminHelpMenu() {
        SendMessage expected = new SendMessage(1L, adminMenuText);

        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        SendMessage actual = startController.adminHelpMenu(message);

        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }
}
