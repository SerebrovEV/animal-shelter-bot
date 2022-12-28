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

    private final String aboutDescriptionText = "Приют Лапки Добра - это муниципальный приют для бездомных собак и кошек в Астане. " +
            "В нем живет почти 2500 собак и 150 кошек. Большие и маленькие, пушистые и гладкие, веселые и задумчивые - и на всех одна" +
            " большая мечта - встретить своего Человека и найти Дом.\n" +
            "\n" +
            "Взять домой\n" +
            "\n" +
            "Если вы хотите взять собаку или кошку, не ищите питомник, " +
            "в котором можно купить щенка или котенка - просто свяжитесь с нами, " +
            "и вы обязательно найдете себе самого лучшего друга. Во всем мире это уже стало доброй традицией - человек, " +
            "который решил завести питомца, обращается в приют, чтобы подарить заботу и любовь тому, " +
            "кто уже появился на свет, но ему почему-то досталась нелегкая судьба. Мы поможем вам выбрать животное с учетом ваших пожеланий и предпочтений," +
            " с радостью познакомим со всеми нашими собаками и кошками. Все наши питомцы привиты и стерилизованы.";

    @Test
    void handleDescriptionMessage() {
        SendMessage expected = new SendMessage(1L, aboutDescriptionText);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleDescriptionMessage(message);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(aboutDescriptionText);
    }
    @Test
    void handleCallbackDescriptionMessage() {
        SendMessage expected = new SendMessage(1L, aboutDescriptionText);
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleCallbackDescriptionMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo(aboutDescriptionText);

    }

    @Test
    void handleAddressAndHoursMessage() {
        SendMessage expected = new SendMessage(1L, "");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleAddressAndHoursMessage(message);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }

    @Test
    void handleCallbackAddressAndHoursMessage() {
        SendMessage expected = new SendMessage(1L, "");
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = aboutController.handleCallbackAddressAndHoursMessage(callbackQuery);
        assertThat(actual.getParameters().get("chatId")).isEqualTo(expected.getParameters().get("chatId"));
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }
}