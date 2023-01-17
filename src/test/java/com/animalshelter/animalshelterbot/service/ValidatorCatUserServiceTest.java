package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.CatUser;
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
class ValidatorCatUserServiceTest {

    @InjectMocks
    private ValidatorCatUserService out;

    @Mock
    private CatUserService catUserService;

    @Mock
    User user;

    @Mock
    Message message;
    @Test
    void validateCatUser() {
        CatUser catUser = new CatUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(catUserService.getCatUserByChatId(1L)).thenReturn(null);
        when(catUserService.addCatUser(catUser)).thenReturn(catUser);

        String expected = "Добавлена запись контакта: " + catUser.toStringUser();
        String actual = out.validateCatUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateCatUserIncorrectText() {
        when(message.text()).thenReturn("79871234567 Тест");
        String expected = "Некорректный номер телефона";
        String actual = out.validateCatUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExistingUser(){
        CatUser catUser = new CatUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(catUserService.getCatUserByChatId(1L)).thenReturn(Optional.of(catUser));
        String expected = "Данный пользователь уже есть, свяжитесь с волонтером для уточнения информации";
        String actual = out.validateCatUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDogUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateCatUser(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateUserFromAdmin() {
        CatUser catUser = new CatUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить КП 89871234567 Иван");
        when(catUserService.getCatUserByPhoneNumber(89871234567L)).thenReturn(null);
        when(catUserService.addCatUser(catUser)).thenReturn(catUser);

        String expected = "Добавлена запись контакта: " + catUser + " в базу данных приюта для кошек.";
        String actual = out.validateCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserFromAdminExistingUser(){
        CatUser catUser = new CatUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить КП 89871234567 Иван");
        when(catUserService.getCatUserByPhoneNumber(89871234567L)).thenReturn(Optional.of(catUser));

        String expected = "Данный усыновитель уже есть";
        String actual = out.validateCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserFromAdminIncorrectNumber(){
        when(message.text()).thenReturn("Сохранить КП 79871234567 Иван");
        String expected = "Некорректный номер телефона";
        String actual = out.validateCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdmin() {
        CatUser catUser = new CatUser("Тест", 89871234567L);
        String expected = catUser.toString() + " из базы данных приюта для кошек.";
        when(message.text()).thenReturn("Найти КП 10");
        when(catUserService.getCatUser(10L)).thenReturn(Optional.of(catUser));
        String actual = out.validateGetCatUserFromAdmin(message);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdminNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
        when(message.text()).thenReturn("Найти КП 10");
        when(catUserService.getCatUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateGetCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdminIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateGetCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDeleteUser() {
        CatUser catUser = new CatUser("Тест", 89871234567L);
        String expected = catUser + " удален из базы данных приюта для кошек";
        when(message.text()).thenReturn("Удалить КП 10");
        when(catUserService.getCatUser(10L)).thenReturn(Optional.of(catUser));
        String actual = out.validateDeleteCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDeleteUserNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
        when(message.text()).thenReturn("Удалить КП 10");
        when(catUserService.getCatUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateDeleteCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Удалить и");
        String actual = out.validateDeleteCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUser() {
        CatUser catUser = new CatUser("Тест", 89871234567L);
        CatUser catUser2 = new CatUser("Миша", 89871234562L);
        String expected = catUser2 + " изменен в базе данных приюта для кошек.";
        when(message.text()).thenReturn("Изменить КП 10 89871234562 Миша");
        when(catUserService.getCatUser(10L)).thenReturn(Optional.of(catUser));
        String actual = out.validateEditCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectNumber(){
        String expected = "Некорректный номер телефона";
        when(message.text()).thenReturn("Изменить КП 10 79871234562 Миша");
        String actual = out.validateEditCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
        when(message.text()).thenReturn("Изменить КП 10 89871234562 Миша");
        when(catUserService.getCatUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateEditCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Изменить и");
        String actual = out.validateEditCatUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
}