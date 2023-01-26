package com.animalshelter.animalshelterbot.controller;

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
class AboutControllerTest {
    @InjectMocks
    AboutController aboutController;

    @Mock
    Message message;

    @Mock
    User user;

    @Mock
    CallbackQuery callbackQuery;

    @Mock
    TelegramBotSender telegramBotSender;

    private final String aboutDogDescriptionText = "Приют Лапки Добра для собак - это муниципальный приют для бездомных собак в Астане. " +
            "В нем живет почти 2500 собак. Большие и маленькие, пушистые и гладкие, веселые и задумчивые - и на всех одна" +
            " большая мечта - встретить своего Человека и найти Дом.\n" +
            "\n" +
            "Взять домой\n" +
            "\n" +
            "Если вы хотите взять собаку, не ищите питомник, " +
            "в котором можно купить щенка - просто свяжитесь с нами, " +
            "и вы обязательно найдете себе самого лучшего друга. Во всем мире это уже стало доброй традицией - человек, " +
            "который решил завести питомца, обращается в приют, чтобы подарить заботу и любовь тому, " +
            "кто уже появился на свет, но ему почему-то досталась нелегкая судьба. Мы поможем вам выбрать животное с учетом ваших пожеланий и предпочтений," +
            " с радостью познакомим со всеми нашими собаками. Все наши питомцы привиты и стерилизованы.";

    private final String aboutCatDescriptionText = "Приют Лапки Добра для кошек - это муниципальный приют для кошек в Астане. " +
            "В нем живет почти 150 кошек. Большие и маленькие, пушистые и гладкие, веселые и задумчивые - и на всех одна" +
            " большая мечта - встретить своего Человека и найти Дом.\n" +
            "\n" +
            "Взять домой\n" +
            "\n" +
            "Если вы хотите взять кошку, не ищите питомник, " +
            "в котором можно купить котенка - просто свяжитесь с нами, " +
            "и вы обязательно найдете себе самого лучшего друга. Во всем мире это уже стало доброй традицией - человек, " +
            "который решил завести питомца, обращается в приют, чтобы подарить заботу и любовь тому, " +
            "кто уже появился на свет, но ему почему-то досталась нелегкая судьба. Мы поможем вам выбрать животное с учетом ваших пожеланий и предпочтений," +
            " с радостью познакомим со всеми нашими кошками. Все наши питомцы привиты и стерилизованы.";

    @Test
    public void handleCallbackDogDescriptionMessage() {
        SendMessage expected = new SendMessage(1L, aboutDogDescriptionText);
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleCallbackDogDescriptionMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(aboutDogDescriptionText);

    }
    @Test
    public void handleCallbackCatDescriptionMessage() {
        SendMessage expected = new SendMessage(1L, aboutCatDescriptionText);
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleCallbackCatDescriptionMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(aboutCatDescriptionText);

    }
    @Test
    public void handleCallbackDogAddressAndHoursMessage() {
        SendMessage expected = new SendMessage(1L, "");
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleCallbackDogAddressAndHoursMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }
    @Test
    public void handleCallbackCatAddressAndHoursMessage() {
        SendMessage expected = new SendMessage(1L, "");
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleCallbackCatAddressAndHoursMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }
    @Test
    public void handleDogCarInfoCallbackMessage() {
        SendMessage expected = new SendMessage(1L, "Для оформления пропуска свяжитесь с начальником отдела" +
                " охраны Ивановым Иваном Ивановичем 89871234567 или службой охраны 88125461234");
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleDogCarInfoCallbackMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    public void handleCatCarInfoCallbackMessage() {
        SendMessage expected = new SendMessage(1L, "Для оформления пропуска свяжитесь с начальником отдела охраны Семеновым Семеном Семеновичем 89861234567 " +
                "или службой охраны 88145461234");
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleCatCarInfoCallbackMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));

    }
}