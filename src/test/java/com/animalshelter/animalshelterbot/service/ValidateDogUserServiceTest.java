package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.impl.DogUserService;
import com.animalshelter.animalshelterbot.service.impl.ValidateDogUserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ValidateDogUserServiceTest {
    @InjectMocks
    private ValidateDogUserService out;

    @Mock
    private DogUserService dogUserService;

    @Mock
    User user;

    @Mock
    Message message;
    @Mock
    TelegramBot telegramBot;


    @Test
    void validateDogUser() {
        DogUser dogUser = new DogUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(dogUserService.getUserByChatId(1L)).thenReturn(Optional.empty());
        when(dogUserService.getUserByPhoneNumber(89871234567L)).thenReturn(Optional.empty());
        when(dogUserService.addUser(dogUser)).thenReturn(dogUser);

        String expected = "Добавлена запись контакта: " + dogUser.toStringUser();
        String actual = out.validateUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDogUserIncorrectText() {
        when(message.text()).thenReturn("79871234567 Тест");
        String expected = "Некорректный номер телефона";
        String actual = out.validateUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExistingUser(){
        DogUser dogUser = new DogUser("Тест", 89871234567L, 1L);
        when(message.text()).thenReturn("89871234567 Тест");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(dogUserService.getUserByChatId(1L)).thenReturn(Optional.of(dogUser));
        String expected = "Данный пользователь уже есть, свяжитесь с волонтером для уточнения информации";
        String actual = out.validateUser(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDogUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateUser(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDogUserIdChat() {
        DogUser dogUser = new DogUser("Ирина", 89818231899L);
        String expected = "Обновлена запись контакта: " + dogUser.toStringUser() + ". Спасибо!";

        when(message.text()).thenReturn("Взял собаку 89818231899 Ирина");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(dogUserService.getUserByChatId(1L)).thenReturn(Optional.empty());
        when(dogUserService.getUserByPhoneNumber(anyLong())).thenReturn(Optional.of(dogUser));

        String actual = out.validateUserIdChat(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDogUserIdChatIncorrectNumber(){
        String expected = "Некорректный номер телефона";

        when(message.text()).thenReturn("Взял собаку 79818231899 Ирина");
        String actual = out.validateUserIdChat(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDogUserIdChatIsPresent() {
        DogUser dogUser = new DogUser("Тест", 89871234567L, 1L);
        String expected = "Обновить запись не удалось, свяжитесь с волонтером для уточнения информации";
        when(message.text()).thenReturn("Взял собаку 89818231899 Ирина");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(dogUserService.getUserByChatId(1L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateUserIdChat(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDogUserIdNumberIsNull() {
        DogUser dogUser = new DogUser("Тест", 89871234567L, 1L);
        String expected = "Телефон не найден, свяжитесь с волонтером для уточнения информации";

        when(message.text()).thenReturn("Взял собаку 89818231899 Миша");
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        when(dogUserService.getUserByChatId(1L)).thenReturn(Optional.empty());
        when(dogUserService.getUserByPhoneNumber(anyLong())).thenReturn(Optional.empty());

        String actual = out.validateUserIdChat(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDogUserIdChatIncorrectMatcher() {
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateUserIdChat(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateUserAddFromAdmin(){
        DogUser dogUser = new DogUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить 89871234567 Иван");
        when(dogUserService.getUserByPhoneNumber(89871234567L)).thenReturn(Optional.empty());
        when(dogUserService.addUser(dogUser)).thenReturn(dogUser);

        String expected = "Добавлена запись контакта: " + dogUser + " в базу данных приюта для собак";
        String actual = out.validateUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateAddUserFromAdminExistingUser(){
        DogUser dogUser = new DogUser("Иван", 89871234567L);

        when(message.text()).thenReturn("Сохранить 89871234567 Иван");
        when(dogUserService.getUserByPhoneNumber(89871234567L)).thenReturn(Optional.of(dogUser));

        String expected = "Данный усыновитель уже есть в базе данных приюта для собак";
        String actual = out.validateUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateAddUserFromAdminIncorrectNumber(){
        when(message.text()).thenReturn("Сохранить 79871234567 Иван");
        String expected = "Некорректный номер телефона";
        String actual = out.validateUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateAddUserFromAdminIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Найти и");
        String actual = out.validateUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdmin(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        String expected = dogUser.toString() + " из базы данных приюта для собак.";
        when(message.text()).thenReturn("Найти 10");
        when(dogUserService.getUser(10L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateGetUserFromAdmin(message);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetUserFromAdminNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Найти 10");
        when(dogUserService.getUser(10L)).thenReturn(Optional.empty());
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
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        String expected = dogUser + "удален из базы данных приюта для собак.";
        when(message.text()).thenReturn("Удалить 10");
        when(dogUserService.getUser(10L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateDeleteUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void validateDeleteUserNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Удалить 10");
        when(dogUserService.getUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateDeleteUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Удалить и");
        String actual = out.validateDeleteUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUser(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        DogUser dogUser2 = new DogUser("Миша", 89871234562L);
        String expected = dogUser2 + " изменен в базе данных приюта для собак.";
        when(message.text()).thenReturn("Изменить 10 89871234562 Миша");
        when(dogUserService.getUser(10L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateEditUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectNumber(){
        String expected = "Некорректный номер телефона";
        when(message.text()).thenReturn("Изменить 10 79871234562 Миша");
        String actual = out.validateEditUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Изменить 10 89871234562 Миша");
        when(dogUserService.getUser(10L)).thenReturn(Optional.empty());
        String actual = out.validateEditUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditUserIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Изменить и");
        String actual = out.validateEditUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateCongratulationDogUserFromAdmin(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        dogUser.setId(2L);
        dogUser.setChatId(12L);
        String expected = dogUser + " направлено поздравление.";
        when(message.text()).thenReturn("Поздравить СП 2");
        when(dogUserService.getUser(2L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateCongratulationUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateCongratulationDogUserFromAdminNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Поздравить СП 2");
        when(dogUserService.getUser(2L)).thenReturn(Optional.empty());
        String actual = out.validateCongratulationUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateCongratulationDogUserFromAdminIncorrectId(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        dogUser.setId(2L);
        String expected = "Команда не выполнена! Для корректной работы необходимо попросить усыновителя добавить" +
                " контактные данные через телеграм-бота прописав сообщение:\n Взял собаку 89817885244 Иван";
        when(message.text()).thenReturn("Поздравить СП 2");
        when(dogUserService.getUser(2L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateCongratulationUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateCongratulationDogUserFromAdminIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Изменить и");
        String actual = out.validateCongratulationUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateReturnDogUserFromAdmin(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        dogUser.setId(3L);
        dogUser.setChatId(12L);
        String expected = dogUser + " направлено уведомление.";
        when(message.text()).thenReturn("Неудача СП 3");
        when(dogUserService.getUser(3L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateReturnUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateReturnDogUserFromAdminNotFound(){
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        when(message.text()).thenReturn("Неудача КП 3");
        when(dogUserService.getUser(3L)).thenReturn(Optional.empty());
        String actual = out.validateReturnUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateReturnDogUserFromAdminIncorrectId(){
        DogUser dogUser = new DogUser("Тест", 89871234567L);
        dogUser.setId(2L);
        String expected = "Команда не выполнена! Для корректной работы необходимо попросить усыновителя добавить" +
                " контактные данные через телеграм-бота прописав сообщение:\n Взял собаку 89817885244 Иван";
        when(message.text()).thenReturn("Неудача СП 3");
        when(dogUserService.getUser(3L)).thenReturn(Optional.of(dogUser));
        String actual = out.validateReturnUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateReturnDogUserFromAdminIncorrectMatcher(){
        String expected = "Некорректный запрос";
        when(message.text()).thenReturn("Изменить и");
        String actual = out.validateReturnUserFromAdmin(message);
        assertThat(actual).isEqualTo(expected);
    }

}