package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.service.AdoptedDogService;
import com.animalshelter.animalshelterbot.service.ValidateAdoptedDogService;
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
class AdminDogControllerTest {
    @InjectMocks
    AdminDogController adminDogController;
    @Mock
    ValidateAdoptedDogService validateAdoptedDogService;
    @Mock
    Message message;
    @Mock
    User user;
    @Mock
    AdoptedDogService adoptedDogService;
    private AdoptedDog adoptedDog;
    private final String ADMIN_COMMAND = "Правила по работе с БД приюта для собак: \n" +
            "/infoAboutAdminDog - команды для использования;\n" +
            "Сохранить c Белка - добавить собаку в базу данных приюта;\n" +
            "Найти с 10 - найти собаку с id = 10;\n" +
            "Изменить с 10 Стрелка - изменить собаку с id = 10;\n" +
            "Удалить с 10 - удалить собаку с id = 10;\n" +
            "Усыновить 11 с 10 - усыновить собаку с id = 11, новый хозяин с id = 10;\n" +
            "Вернуть с 10 - вернуть собаку с id = 10 в приют от плохого усыновителя;\n" +
            "Продлить с 2 на 14 (30) - продлить период адаптации собаки с id=2 на 14 дней(или на 30 дней) для плохого усыновителя;\n" +
            "/getAllDog - получить список всех собак;\n" +
            "/getAllFreeDog - получить список всех свободных собак в приюте;\n" +
            "/getAllDogOnTrialPeriod  - получить список всех собак на испытательном периоде;\n" +
            "/getAllDogWithEndPeriod - получить список всех собак с окончаниям испытательного срока.";

    @BeforeEach
    void setUp() {
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        adoptedDog = new AdoptedDog("С");
    }

    @Test
    void handleInfoAboutAdminDog() {
        SendMessage expected = new SendMessage(1L, ADMIN_COMMAND);
        SendMessage actual = adminDogController.handleInfoAboutAdminDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(ADMIN_COMMAND);
    }

    @Test
    void handleCreateDog() {
        SendMessage expected = new SendMessage(1L, adoptedDog.toString());
        when(validateAdoptedDogService.validateAddDog(message)).thenReturn(adoptedDog.toString());
        SendMessage actual = adminDogController.handleCreateDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(adoptedDog.toString());
    }

    @Test
    void handleDeleteDog() {
        SendMessage expected = new SendMessage(1L, adoptedDog.toString());
        when(validateAdoptedDogService.validateDeleteDog(any())).thenReturn(adoptedDog.toString());
        SendMessage actual = adminDogController.handleDeleteDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(adoptedDog.toString());
    }

    @Test
    void handleGetDog() {
        SendMessage expected = new SendMessage(1L, adoptedDog.toString());
        when(validateAdoptedDogService.validateGetDog(any())).thenReturn(adoptedDog.toString());
        SendMessage actual = adminDogController.handleGetDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(adoptedDog.toString());
    }

    @Test
    void handleEditDog() {
        SendMessage expected = new SendMessage(1L, adoptedDog.toString());
        when(validateAdoptedDogService.validateEditDog(any())).thenReturn(adoptedDog.toString());
        SendMessage actual = adminDogController.handleEditDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(adoptedDog.toString());
    }

    @Test
    void handleGetAllDog() {
        AdoptedDog dog1 = new AdoptedDog("C1");
        AdoptedDog dog2 = new AdoptedDog("C2");
        AdoptedDog dog3 = new AdoptedDog("C3");
        List<AdoptedDog> dogs = List.of(adoptedDog, dog1, dog2, dog3);
        when(adoptedDogService.getAllDog()).thenReturn(dogs);

        List<SendMessage> expected = List.of(
                new SendMessage(1L, adoptedDog.toString()),
                new SendMessage(1L, dog1.toString()),
                new SendMessage(1L, dog2.toString()),
                new SendMessage(1L, dog3.toString()));

        List<SendMessage> actual = adminDogController.handleGetAllDog(message);

        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i).getParameters().get("idUser")).isEqualTo(expected.get(i).getParameters().get("idUser"));
            assertThat(actual.get(i).getParameters().get("text")).isEqualTo(expected.get(i).getParameters().get("text"));
        }
    }

    @Test
    void handleGetAllFreeDog() {
        AdoptedDog dog1 = new AdoptedDog("C1");
        AdoptedDog dog2 = new AdoptedDog("C2");
        AdoptedDog dog3 = new AdoptedDog("C3");
        List<AdoptedDog> dogs = List.of(adoptedDog, dog1, dog2, dog3);
        when(adoptedDogService.getAllFreeDog()).thenReturn(dogs);

        List<SendMessage> expected = List.of(
                new SendMessage(1L, adoptedDog.toString()),
                new SendMessage(1L, dog1.toString()),
                new SendMessage(1L, dog2.toString()),
                new SendMessage(1L, dog3.toString()));

        List<SendMessage> actual = adminDogController.handleGetAllFreeDog(message);

        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i).getParameters().get("idUser")).isEqualTo(expected.get(i).getParameters().get("idUser"));
            assertThat(actual.get(i).getParameters().get("text")).isEqualTo(expected.get(i).getParameters().get("text"));

        }
    }

    @Test
    void handleGetAllDogOnTrialPeriod() {
        AdoptedDog dog1 = new AdoptedDog("C1");
        AdoptedDog dog2 = new AdoptedDog("C2");
        AdoptedDog dog3 = new AdoptedDog("C3");
        List<AdoptedDog> dogs = List.of(adoptedDog, dog1, dog2, dog3);
        when(adoptedDogService.getAllDogOnTrialPeriod()).thenReturn(dogs);

        List<SendMessage> expected = List.of(
                new SendMessage(1L, adoptedDog.toString()),
                new SendMessage(1L, dog1.toString()),
                new SendMessage(1L, dog2.toString()),
                new SendMessage(1L, dog3.toString()));

        List<SendMessage> actual = adminDogController.handleGetAllDogOnTrialPeriod(message);

        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i).getParameters().get("idUser")).isEqualTo(expected.get(i).getParameters().get("idUser"));
            assertThat(actual.get(i).getParameters().get("text")).isEqualTo(expected.get(i).getParameters().get("text"));
        }
    }

    @Test
    void handleGetAllDogWithEndPeriod() {
        AdoptedDog dog1 = new AdoptedDog("C1");
        AdoptedDog dog2 = new AdoptedDog("C2");
        AdoptedDog dog3 = new AdoptedDog("C3");
        List<AdoptedDog> dogs = List.of(adoptedDog, dog1, dog2, dog3);
        when(adoptedDogService.getAllDogWithEndPeriod()).thenReturn(dogs);

        List<SendMessage> expected = List.of(
                new SendMessage(1L, adoptedDog.toString()),
                new SendMessage(1L, dog1.toString()),
                new SendMessage(1L, dog2.toString()),
                new SendMessage(1L, dog3.toString()));

        List<SendMessage> actual = adminDogController.handleGetAllDogWithEndPeriod(message);

        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i).getParameters().get("idUser")).isEqualTo(expected.get(i).getParameters().get("idUser"));
            assertThat(actual.get(i).getParameters().get("text")).isEqualTo(expected.get(i).getParameters().get("text"));
        }

    }

    @Test
    void handleTakeDog() {
        SendMessage expected = new SendMessage(1L, adoptedDog.toString());
        when(validateAdoptedDogService.validateTakeDog(any())).thenReturn(adoptedDog.toString());
        SendMessage actual = adminDogController.handleTakeDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(adoptedDog.toString());
    }

    @Test
    void handleReturnDog() {
        SendMessage expected = new SendMessage(1L, adoptedDog.toString());
        when(validateAdoptedDogService.validateReturnDog(any())).thenReturn(adoptedDog.toString());
        SendMessage actual = adminDogController.handleReturnDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(adoptedDog.toString());
    }

    @Test
    void handleExtendCDog() {
        SendMessage expected = new SendMessage(1L, adoptedDog.toString());
        when(validateAdoptedDogService.validateExtendDog(any())).thenReturn(adoptedDog.toString());
        SendMessage actual = adminDogController.handleExtendCDog(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(adoptedDog.toString());
    }
}