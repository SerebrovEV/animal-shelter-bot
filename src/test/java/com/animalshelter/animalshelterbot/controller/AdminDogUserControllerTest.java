package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.DogUserService;
import com.animalshelter.animalshelterbot.service.ValidatorDogUserService;
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
class AdminDogUserControllerTest {

    @InjectMocks
    AdminDogUserController out;
    @Mock
    ValidatorDogUserService validatorDogUserService;
    @Mock
    Message message;
    @Mock
    User user;

    @Mock
    DogUserService dogUserService;
    private DogUser DOG_USER;

    private final String ADMIN_COMMAND = "Правила использования: \n" +
            "/infoAboutAdminDogUser - команды для использования;\n" +
            "Сохранить СП 89871234567 Иван - добавить усыновителя;\n" +
            "Найти СП 10 - найти усыновителя с id = 10;\n" +
            "Изменить СП 10 89871234567 Миша - изменить усыновителя с id = 10;\n" +
            "Удалить СП 10 - удалить усыновителя с id = 10;\n" +
            "Поздравить CП 2 - поздравить усыновителя с id = 2 с окончанием испытательного срока;\n" +
            "Неудача CП 3 - направить усыновителю с id = 3 сообщение о том, что он не прошел испытательный срок;\n" +
            "/getAllDogUser - получить список всех усыновителей;\n";

    @BeforeEach
    public void setOut() {
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        DOG_USER = new DogUser("Test", 89871234567L);
    }

    @Test
    void handleInfoAboutAdminDogUser() {
        SendMessage expected = new SendMessage(1L, ADMIN_COMMAND);
        SendMessage actual = out.handleInfoAboutAdminDogUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleCreateDogUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, dogUser.toString());
        when(validatorDogUserService.validateDogUserFromAdmin(message)).thenReturn(dogUser.toString());
        SendMessage actual = out.handleCreateDogUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetDogUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        when(validatorDogUserService.validateGetDogUserFromAdmin(any())).thenReturn(dogUser.toString());

        SendMessage expected = new SendMessage(1L, dogUser.toString());

        SendMessage actual = out.handleGetDogUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleDeleteDogUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, dogUser.toString());
        when(validatorDogUserService.validateDeleteDogUserFromAdmin(any())).thenReturn(dogUser.toString());

        SendMessage actual = out.handleDeleteDogUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleEditDogUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        SendMessage expected = new SendMessage(1L, dogUser.toString());
        when(validatorDogUserService.validateEditDogUserFromAdmin(message)).thenReturn(dogUser.toString());

        SendMessage actual = out.handleEditDogUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetAllDogUser() {
        DogUser dogUser = new DogUser("Test", 89871234567L);
        DogUser dogUser2 = new DogUser("Test2", 89871234568L);
        DogUser dogUser3 = new DogUser("Test3", 89871234569L);
        DogUser dogUser4 = new DogUser("Test4", 89871234561L);
        List<DogUser> dogUsers = List.of(dogUser, dogUser2, dogUser3, dogUser4);
        when(dogUserService.getAllDogUser()).thenReturn(dogUsers);

        SendMessage expected = new SendMessage(1L, List.of(dogUser, dogUser2, dogUser3, dogUser4).toString());
        SendMessage actual = out.handleGetAllDogUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));

    }
    @Test
    void handleCongratulationDogUser() {
        SendMessage expected = new SendMessage(1L, DOG_USER.toString());
        when(validatorDogUserService.validateCongratulationDogUserFromAdmin(message)).thenReturn(DOG_USER.toString());

        SendMessage actual = out.handleCongratulationDogUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleReturnDogUser() {
        SendMessage expected = new SendMessage(1L, DOG_USER.toString());
        when(validatorDogUserService.validateReturnDogUserFromAdmin(message)).thenReturn(DOG_USER.toString());

        SendMessage actual = out.handleReturnDogUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

}