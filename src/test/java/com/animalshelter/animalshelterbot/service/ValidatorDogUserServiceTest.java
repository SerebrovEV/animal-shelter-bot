package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.DogUser;
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
class ValidatorDogUserServiceTest {
    @InjectMocks
    private ValidatorDogUserService out;

    @Mock
    private DogUserService dogUserService;

    @Mock
    User user;

    @Mock
    Message message;


    @Test
    void validateUser() {
        DogUser dogUser = new DogUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(dogUserService.getDogUserByChatId(1L)).thenReturn(null);
        when(dogUserService.addDogUser(dogUser)).thenReturn(dogUser);

        String expected = "Добавлена запись контакта: " + dogUser.toStringUser();
        String actual = out.validateDogUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserIncorrectText() {
        when(message.text()).thenReturn("79871234567 Тест");
        String expected = "Некорректный номер телефона";
        String actual = out.validateDogUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExistingUser(){
        DogUser dogUser = new DogUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(dogUserService.getDogUserByChatId(1L)).thenReturn(dogUser);
        String expected = "Данный пользователь уже есть, свяжитесь с волонтером для уточнения информации";
        String actual = out.validateDogUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDogUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateDogUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserFromAdmin(){
        DogUser dogUser = new DogUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить 89871234567 Иван");
        when(dogUserService.getDogUserByPhoneNumber(89871234567L)).thenReturn(null);
        when(dogUserService.addDogUser(dogUser)).thenReturn(dogUser);

        String expected = "Добавлена запись контакта: " + dogUser + " в базу данных приюта для собак";
        String actual = out.validateDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserFromAdminExistingUser(){
        DogUser dogUser = new DogUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить 89871234567 Иван");
        when(dogUserService.getDogUserByPhoneNumber(89871234567L)).thenReturn(dogUser);

        String expected = "Данный усыновитель уже есть в базе данных приюта для собак";
        String actual = out.validateDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserFromAdminIncorrectNumber(){
        when(message.text()).thenReturn("Сохранить 79871234567 Иван");
        String expected = "Некорректный номер телефона";
        String actual = out.validateDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateUserFromAdminIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdmin(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        String expected = dogUser.toString() + " из базы данных приюта для собак.";
        when(message.text()).thenReturn("Найти 10");
        when(dogUserService.getDogUser(10L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateGetDogUserFromAdmin(message);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdminNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Найти 10");
        when(dogUserService.getDogUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateGetDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdminIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateGetDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteUser(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        String expected = dogUser + "удален из базы данных приюта для собак.";
        when(message.text()).thenReturn("Удалить 10");
        when(dogUserService.getDogUser(10L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateDeleteDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void validateDeleteUserNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Удалить 10");
        when(dogUserService.getDogUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateDeleteDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Удалить и");
        String actual = out.validateDeleteDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUser(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        DogUser dogUser2 = new DogUser("Миша", 89871234562L);
        String expected = dogUser2 + " изменен в базе данных приюта для собак.";
        when(message.text()).thenReturn("Изменить 10 89871234562 Миша");
        when(dogUserService.getDogUser(10L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateEditDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectNumber(){
        String expected = "Некорректный номер телефона";
        when(message.text()).thenReturn("Изменить 10 79871234562 Миша");
        String actual = out.validateEditDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Изменить 10 89871234562 Миша");
        when(dogUserService.getDogUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateEditDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Изменить и");
        String actual = out.validateEditDogUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }



}