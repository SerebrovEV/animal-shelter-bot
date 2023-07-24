package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.impl.AdoptedDogService;
import com.animalshelter.animalshelterbot.service.impl.DogUserService;
import com.animalshelter.animalshelterbot.service.impl.ValidateAdoptedDogService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateAdoptedDogServiceTest {
    @InjectMocks
    ValidateAdoptedDogService validateAdoptedDogService;
    @Mock
    AdoptedDogService adoptedDogService;
    @Mock
    DogUserService dogUserService;
    @Mock
    Message message;
    @Mock
    TelegramBot telegramBot;

    @Test
    void validateAddDog() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        when(message.text()).thenReturn("Сохранить с Стеша");
        when(adoptedDogService.addPet(any())).thenReturn(adoptedDog);
        String expected = "Добавлена запись собаки в базу данных приюта для собак: " + adoptedDog.getDogName();
        String actual = validateAdoptedDogService.validateAddPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateAddDogIncorrectRequest() {
        when(message.text()).thenReturn("Сохранить с");
        String expected = "Некорректный запрос";
        String actual = validateAdoptedDogService.validateAddPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateDeleteDog() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        when(message.text()).thenReturn("Удалить с 1");

        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));
        String expected = adoptedDog + " удалена из базы данных приюта для собак.";
        String actual = validateAdoptedDogService.validateDeletePet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteDogNotFound(){
        when(message.text()).thenReturn("Удалить с 1");

        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.empty());
        String expected = "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
        String actual = validateAdoptedDogService.validateDeletePet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateDeleteDogIncorrectRequest() {
        when(message.text()).thenReturn("Удалить с");
        String expected = "Некорректный запрос";
        String actual = validateAdoptedDogService.validateDeletePet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetDog() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        when(message.text()).thenReturn("Найти с 1");

        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));
        String expected = adoptedDog + " из базы данных приюта для собак.";
        String actual = validateAdoptedDogService.validateGetPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateGetDogNotFound(){
        when(message.text()).thenReturn("Найти с 1");

        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.empty());
        String expected = "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
        String actual = validateAdoptedDogService.validateGetPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateGetDogIncorrectRequest() {
        when(message.text()).thenReturn("Найти с");
        String expected = "Некорректный запрос";
        String actual = validateAdoptedDogService.validateGetPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditDog() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        adoptedDog.setId(4L);
        AdoptedDog adoptedDog1 = new AdoptedDog("Стефания");
        adoptedDog1.setId(4L);
        when(message.text()).thenReturn("Изменить с 4 Стефания");

        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));
        String expected = adoptedDog1 + " изменен в базе данных приюта для собак.";
        String actual = validateAdoptedDogService.validateEditPet(message);
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    void validateEditDogNotFound(){
        when(message.text()).thenReturn("Изменить с 1 Стефания");

        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.empty());
        String expected = "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
        String actual = validateAdoptedDogService.validateEditPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateEditDogIncorrectRequest() {
        when(message.text()).thenReturn("Изменить с");
        String expected = "Некорректный запрос";
        String actual = validateAdoptedDogService.validateEditPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateTakeDog() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        DogUser dogUser = new DogUser("Иван", 10);
        dogUser.setId(3L);
        adoptedDog.setId(4L);
        adoptedDog.setDogUser(dogUser);
        adoptedDog.setTrialPeriod(30);
        adoptedDog.setAdoptionDate(Date.valueOf(LocalDate.now()));
        when(message.text()).thenReturn("Усыновить 3 с 4");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));
        when(dogUserService.getUser(anyLong())).thenReturn(Optional.of(dogUser));
        String expected = adoptedDog.toString();
        String actual = validateAdoptedDogService.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateTakeDogIncorrectRequest() {
        when(message.text()).thenReturn("Усыновить с");
        String expected = "Некорректный запрос";
        String actual = validateAdoptedDogService.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateTakeDogNotFound() {
        when(message.text()).thenReturn("Усыновить 11 к 10");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.empty());
        String expected = "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
        String actual = validateAdoptedDogService.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateTakeDogNotFoundUser() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        when(message.text()).thenReturn("Усыновить 11 к 10");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));
        when(dogUserService.getUser(anyLong())).thenReturn(Optional.empty());
        String expected = "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
        String actual = validateAdoptedDogService.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateReturnDog() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        adoptedDog.setTrialPeriod(30);
        when(message.text()).thenReturn("Вернуть c 10");
        when(adoptedDogService.getPet(10L)).thenReturn(Optional.of(adoptedDog));
        String expected = adoptedDog + " изменен в базе данных приюта для собак.";
        String actual = validateAdoptedDogService.validateReturnPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateReturnDogIncorrectRequest() {
        when(message.text()).thenReturn("Вернуть с");
        String expected = "Некорректный запрос";
        String actual = validateAdoptedDogService.validateReturnPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateReturnDogNotFound() {
        when(message.text()).thenReturn("Вернуть с 10");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.empty());
        String expected = "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
        String actual = validateAdoptedDogService.validateReturnPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExtendDog() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        AdoptedDog adoptedDog1 = new AdoptedDog("Стеша");
        adoptedDog.setTrialPeriod(30);
        adoptedDog1.setTrialPeriod(44);
        adoptedDog.setDogUser(new DogUser("Иван", 10L, 1L));
        adoptedDog1.setDogUser(new DogUser("Иван", 10L, 1L));

        when(message.text()).thenReturn("Продлить c 1 на 14");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));
        String expected = adoptedDog1 + " изменен в базе данных приюта для собак.";
        String actual = validateAdoptedDogService.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
        when(message.text()).thenReturn("Продлить c 1 на 30");
        adoptedDog1.setTrialPeriod(60);
        expected = adoptedDog1 + " изменен в базе данных приюта для собак.";
        adoptedDog.setTrialPeriod(30);
        actual = validateAdoptedDogService.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateExtendDogIncorrectDaySet() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        adoptedDog.setDogUser(new DogUser("Иван", 10L, 1L));

        when(message.text()).thenReturn("Продлить с 2 на 25");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));
        String expected = "Некорректный запрос на добавление дней к периоду адаптации, можно добавить либо 14, либо 30 дней.";
        String actual = validateAdoptedDogService.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateExtendDogNotIdChat() {
        AdoptedDog adoptedDog = new AdoptedDog("Стеша");
        adoptedDog.setDogUser(new DogUser("Иван", 10));

        when(message.text()).thenReturn("Продлить c 2 на 14");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.of(adoptedDog));

        String expected = "Продление не выполнено! Для корректной работы необходимо попросить усыновителя добавить" +
                " контактные данные через телеграм-бота прописав сообщение:\n Взял собаку 89817885244 Иван";
        String actual = validateAdoptedDogService.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateExtendDogNotFound() {
        when(message.text()).thenReturn("Продлить c 2 на 14");
        when(adoptedDogService.getPet(anyLong())).thenReturn(Optional.empty());
        String expected = "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
        String actual = validateAdoptedDogService.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateExtendDogIncorrectRequest() {
        when(message.text()).thenReturn("Продлить с на");
        String expected = "Некорректный запрос";
        String actual = validateAdoptedDogService.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }


}