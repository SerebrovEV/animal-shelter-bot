package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.service.AdoptedCatService;
import com.animalshelter.animalshelterbot.service.ValidateAdoptedCatService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
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
class AdminCatControllerTest {

    @InjectMocks
    AdminCatController out;
    @Mock
    ValidateAdoptedCatService validateAdoptedCatService;
    @Mock
    Message message;
    @Mock
    User user;

    @Mock
    AdoptedCatService adoptedCatService;

    private AdoptedCat adoptedCat;

    private final String ADMIN_COMMAND = "Правила работы с кошками: \n" +
            "/infoAboutAdminCat - команды для использования;\n" +
            "Сохранить к Мурзик - добавить кошку в базу данных приюта;\n" +
            "Найти к 10 - найти кошку с id = 10;\n" +
            "Изменить к 10 Мила - изменить кошку с id = 10;\n" +
            "Удалить к 10 - удалить кошку с id = 10;\n" +
            "Усыновить 11 к 10 - усыновить кошку с id = 11, новый хозяин с id = 10;\n" +
            "Вернуть к 10 - вернуть кошку с id = 10 в приют от плохого усыновителя;\n" +
            "Продлить к 2 на 14 (30) - продлить период адаптации кошке с id=2 на 14 дней(или на 30 дней) для плохого усыновителя;\n" +
            "/getAllCat - получить список всех кошек;\n" +
            "/getAllFreeCat - получить список всех свободных кошек в приюте;\n" +
            "/getAllBusyCat  - получить список всех кошек на испытательном периоде.";

    @BeforeEach
    public void setOut() {
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        adoptedCat = new AdoptedCat("Test");
    }

    @Test
    void handleInfoAboutAdminCat() {
        SendMessage expected = new SendMessage(1L, ADMIN_COMMAND);
        SendMessage actual = out.handleInfoAboutAdminCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleCreateCat() {
        SendMessage expected = new SendMessage(1L, adoptedCat.toString());
        when(validateAdoptedCatService.validateAddCat(message)).thenReturn(adoptedCat.toString());
        SendMessage actual = out.handleCreateCat(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleDeleteCat() {
        SendMessage expected = new SendMessage(1L, adoptedCat.toString());
        when(validateAdoptedCatService.validateDeleteCat(any())).thenReturn(adoptedCat.toString());

        SendMessage actual = out.handleDeleteCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetCat() {
        SendMessage expected = new SendMessage(1L, adoptedCat.toString());
        when(validateAdoptedCatService.validateGetCat(any())).thenReturn(adoptedCat.toString());

        SendMessage actual = out.handleGetCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));

    }

    @Test
    void handleEditCat() {
        SendMessage expected = new SendMessage(1L, adoptedCat.toString());
        when(validateAdoptedCatService.validateEditCat(any())).thenReturn(adoptedCat.toString());

        SendMessage actual = out.handleEditCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetAllCat() {
        AdoptedCat cat2 = new AdoptedCat("Test2");
        AdoptedCat cat3 = new AdoptedCat("Test3");
        AdoptedCat cat4 = new AdoptedCat("Test4");
        List<AdoptedCat> cats = List.of(adoptedCat, cat2, cat3, cat4);
        when(adoptedCatService.getAllCat()).thenReturn(cats);

        SendMessage expected = new SendMessage(1L, List.of(adoptedCat, cat2, cat3, cat4).toString());
        SendMessage actual = out.handleGetAllCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetAllFreeCat() {
        AdoptedCat cat2 = new AdoptedCat("Test2");
        AdoptedCat cat3 = new AdoptedCat("Test3");
        AdoptedCat cat4 = new AdoptedCat("Test4");
        List<AdoptedCat> cats = List.of(adoptedCat, cat2, cat3, cat4);
        when(adoptedCatService.getAllFreeCat()).thenReturn(cats);

        SendMessage expected = new SendMessage(1L, List.of(adoptedCat, cat2, cat3, cat4).toString());
        SendMessage actual = out.handleGetAllFreeCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetAllBusyCat() {
        AdoptedCat cat2 = new AdoptedCat("Test2");
        AdoptedCat cat3 = new AdoptedCat("Test3");
        AdoptedCat cat4 = new AdoptedCat("Test4");
        List<AdoptedCat> cats = List.of(adoptedCat, cat2, cat3, cat4);
        when(adoptedCatService.getAllBusyCat()).thenReturn(cats);

        SendMessage expected = new SendMessage(1L, List.of(adoptedCat, cat2, cat3, cat4).toString());
        SendMessage actual = out.handleGetAllBusyCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleTakeCat() {
        SendMessage expected = new SendMessage(1L, adoptedCat.toString());
        when(validateAdoptedCatService.validateTakeCat(any())).thenReturn(adoptedCat.toString());

        SendMessage actual = out.handleTakeCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleReturnCat() {
        SendMessage expected = new SendMessage(1L, adoptedCat.toString());
        when(validateAdoptedCatService.validateReturnCat(any())).thenReturn(adoptedCat.toString());

        SendMessage actual = out.handleReturnCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleExtendCat() {
        SendMessage expected = new SendMessage(1L, adoptedCat.toString());
        when(validateAdoptedCatService.validateExtendCat(any())).thenReturn(adoptedCat.toString());

        SendMessage actual = out.handleExtendCat(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

}