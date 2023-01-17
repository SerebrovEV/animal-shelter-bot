package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.service.CatUserService;
import com.animalshelter.animalshelterbot.service.ValidatorCatUserService;
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
class AdminCatUserControllerTest {

    @InjectMocks
    AdminCatUserController out;
    @Mock
    ValidatorCatUserService validatorCatUserService;
    @Mock
    Message message;
    @Mock
    User user;

    @Mock
    CatUserService catUserService;

    private CatUser CAT_USER;

    private final String ADMIN_COMMAND = "Команды для работы с усыновителями кошек: \n" +
            "/infoAboutAdminCatUser - команды для использования;\n" +
            "Сохранить КП 89871234567 Иван - добавить усыновителя;\n" +
            "Найти КП 10 - найти усыновителя с id = 10;\n" +
            "Изменить КП 10 89871234567 Миша - изменить усыновителя с id = 10;\n" +
            "Удалить КП 10 - удалить усыновителя с id = 10;\n" +
            "Поздравить КП 2 - поздравить усыновителя с id = 2 с окончанием испытательного срока;\n" +
            "Неудача КП 3 - направить усыновителю с id = 3 сообщение о том, что он не прошел испытательный срок;\n" +
            "/getAllCatUser - получить список всех усыновителей;\n";

    @BeforeEach
    public void setOut() {
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        CAT_USER = new CatUser("Test", 89871234567L);
    }

    @Test
    void handleInfoAboutAdminCatUser() {
        SendMessage expected = new SendMessage(1L, ADMIN_COMMAND);
        SendMessage actual = out.handleInfoAboutAdminCatUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));

    }

    @Test
    void handleCreateCatUser() {
        SendMessage expected = new SendMessage(1L, CAT_USER.toString());
        when(validatorCatUserService.validateCatUserFromAdmin(message)).thenReturn(CAT_USER.toString());
        SendMessage actual = out.handleCreateCatUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetCatUser() {
        when(validatorCatUserService.validateGetCatUserFromAdmin(any())).thenReturn(CAT_USER.toString());

        SendMessage expected = new SendMessage(1L, CAT_USER.toString());

        SendMessage actual = out.handleGetCatUser(message);
        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleDeleteCatUser() {

        SendMessage expected = new SendMessage(1L, CAT_USER.toString());
        when(validatorCatUserService.validateDeleteCatUserFromAdmin(any())).thenReturn(CAT_USER.toString());

        SendMessage actual = out.handleDeleteCatUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleEditCatUser() {

        SendMessage expected = new SendMessage(1L, CAT_USER.toString());
        when(validatorCatUserService.validateEditCatUserFromAdmin(message)).thenReturn(CAT_USER.toString());

        SendMessage actual = out.handleEditCatUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleGetAllCatUser() {
        CatUser catUser2 = new CatUser("Test2", 89871234568L);
        CatUser catUser3 = new CatUser("Test3", 89871234569L);
        CatUser catUser4 = new CatUser("Test4", 89871234561L);
        List<CatUser> catUsers = List.of(CAT_USER, catUser2, catUser3, catUser4);
        when(catUserService.getAllCatUser()).thenReturn(catUsers);

        List<SendMessage> expected = List.of(
                new SendMessage(1L, CAT_USER.toString()),
                new SendMessage(1L, catUser2.toString()),
                new SendMessage(1L, catUser3.toString()),
                new SendMessage(1L, catUser4.toString()));
        List<SendMessage> actual = out.handleGetAllCatUser(message);

        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i).getParameters().get("idUser")).isEqualTo(expected.get(i).getParameters().get("idUser"));
            assertThat(actual.get(i).getParameters().get("text")).isEqualTo(expected.get(i).getParameters().get("text"));
        }
    }

    @Test
    void handleCongratulationCatUser() {
        SendMessage expected = new SendMessage(1L, CAT_USER.toString());
        when(validatorCatUserService.validateCongratulationCatUserFromAdmin(message)).thenReturn(CAT_USER.toString());

        SendMessage actual = out.handleCongratulationCatUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleReturnCatUser() {
        SendMessage expected = new SendMessage(1L, CAT_USER.toString());
        when(validatorCatUserService.validateReturnCatUserFromAdmin(message)).thenReturn(CAT_USER.toString());

        SendMessage actual = out.handleReturnCatUser(message);

        assertThat(actual.getParameters().get("idUser")).isEqualTo(expected.getParameters().get("idUser"));
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }
}