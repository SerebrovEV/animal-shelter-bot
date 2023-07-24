package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.service.impl.AdoptedCatService;
import com.animalshelter.animalshelterbot.service.impl.CatUserService;
import com.animalshelter.animalshelterbot.service.impl.ValidateAdoptedCatService;
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
/**
 * Тесты для проверки работоспособности {@link ValidateAdoptedCatService}
 */
@ExtendWith(MockitoExtension.class)
class ValidateAdoptedCatServiceTest {
    @InjectMocks
    ValidateAdoptedCatService out;

    @Mock
    AdoptedCatService adoptedCatService;
    @Mock
    CatUserService catUserService;

    @Mock
    Message message;

    @Mock
    TelegramBot telegramBot;

    @Test
    void validateAddCat() {

        AdoptedCat adoptedCat = new AdoptedCat("Тест");

        when(message.text()).thenReturn("Сохранить к Тест");
        when(adoptedCatService.addPet(any())).thenReturn(adoptedCat);

        String expected = "Добавлена запись кота в базу данных приюта для кошек: " + adoptedCat.getCatName();
        String actual = out.validateAddPet(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateAddCatIncorrectText() {

        when(message.text()).thenReturn("Сохранить к");
        String expected = "Некорректный запрос";
        String actual = out.validateAddPet(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateDeleteCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");

        when(message.text()).thenReturn("Удалить к 1");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected = adoptedCat + " удалена из базы данных приюта для кошек.";
        String actual = out.validateDeletePet(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateDeleteCatNotFound() {

        when(message.text()).thenReturn("Удалить к 1");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.empty());

        String expected = "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateDeletePet(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateDeleteCatIncorrectText() {

        when(message.text()).thenReturn("Удалить к");
        String expected = "Некорректный запрос";
        String actual = out.validateDeletePet(message);
        assertThat(actual).isEqualTo(expected);

    }


    @Test
    void validateGetCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");

        when(message.text()).thenReturn("Найти к 1");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected = adoptedCat + " из базы данных приюта для кошек.";
        String actual = out.validateGetPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetCatNotFound() {

        when(message.text()).thenReturn("Найти к 1");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.empty());

        String expected = "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateGetPet(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateGetCatIncorrectText() {

        when(message.text()).thenReturn("Удалить к");
        String expected = "Некорректный запрос";
        String actual = out.validateGetPet(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateEditCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        adoptedCat.setId(1L);

        when(message.text()).thenReturn("Изменить к 1 Тест2");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected = adoptedCat + " изменен в базе данных приюта для кошек.";
        String actual = out.validateEditPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditCatNotFound() {

        when(message.text()).thenReturn("Изменить к 1 Тест2");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.empty());

        String expected = "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateEditPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditCatIncorrectText() {

        when(message.text()).thenReturn("Изменить к");

        String expected = "Некорректный запрос";
        ;
        String actual = out.validateEditPet(message);
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void validateTakeCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        CatUser catUser = new CatUser("ТестЮзер", 10L);
        catUser.setId(1L);
        adoptedCat.setId(1L);
        adoptedCat.setCatUser(catUser);
        adoptedCat.setTrialPeriod(30);
        adoptedCat.setAdoptionDate(Date.valueOf(LocalDate.now()));


        when(message.text()).thenReturn("Усыновить 11 к 10");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));
        when(catUserService.getUser(anyLong())).thenReturn(Optional.of(catUser));

        String expected = adoptedCat.toString();
        String actual = out.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateTakeCatIncorrectText() {
        when(message.text()).thenReturn("Изменить к");
        String expected = "Некорректный запрос";
        String actual = out.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateTakeCatNotFound() {
        when(message.text()).thenReturn("Усыновить 11 к 10");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.empty());

        String expected = "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateTakeCatNotFoundUser() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        when(message.text()).thenReturn("Усыновить 11 к 10");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));
        when(catUserService.getUser(anyLong())).thenReturn(Optional.empty());

        String expected = "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateTakePet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateReturnCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        adoptedCat.setTrialPeriod(30);

        when(message.text()).thenReturn("Вернуть к 10");
        when(adoptedCatService.getPet(10L)).thenReturn(Optional.of(adoptedCat));

        String expected = adoptedCat + " изменен в базе данных приюта для кошек.";
        String actual = out.validateReturnPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateReturnCatNotFound() {
        when(message.text()).thenReturn("Вернуть к 10");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.empty());

        String expected = "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateReturnPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateReturnCatIncorrectText() {
        when(message.text()).thenReturn("Изменить к");
        String expected = "Некорректный запрос";
        String actual = out.validateReturnPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateExtendCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        AdoptedCat adoptedCat2 = new AdoptedCat("Тест");
        adoptedCat.setTrialPeriod(30);
        adoptedCat2.setTrialPeriod(44);
        adoptedCat.setCatUser(new CatUser("Тест", 10L, 1L));
        adoptedCat2.setCatUser(new CatUser("Тест", 10L, 1L));

        when(message.text()).thenReturn("Продлить к 2 на 14");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected = adoptedCat2 + " изменен в базе данных приюта для кошек.";
        String actual = out.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);

        when(message.text()).thenReturn("Продлить к 2 на 30");
        adoptedCat2.setTrialPeriod(60);
        expected = adoptedCat2 + " изменен в базе данных приюта для кошек.";
        adoptedCat.setTrialPeriod(30);

        actual = out.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExtendCatIncorrectDaySet() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        adoptedCat.setCatUser(new CatUser("Тест", 10L, 1L));

        when(message.text()).thenReturn("Продлить к 2 на 25");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected = "Некорректный запрос на добавление дней к периоду адаптации, можно добавить либо 14, либо 30 дней.";
        String actual = out.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExtendCatNotIdChat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        adoptedCat.setCatUser(new CatUser("Тест", 10L));

        when(message.text()).thenReturn("Продлить к 2 на 25");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected = "Продление не выполнено! Для корректной работы необходимо попросить усыновителя добавить" +
                " контактные данные через телеграм-бота прописав сообщение:\n Взял кота 89817885244 Иван";
        String actual = out.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void validateExtendCatNotFound() {
        when(message.text()).thenReturn("Продлить к 2 на 14");
        when(adoptedCatService.getPet(anyLong())).thenReturn(Optional.empty());
        String expected = "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateExtendCatIncorrectText() {
        when(message.text()).thenReturn("Изменить к");
        String expected = "Некорректный запрос";
        String actual = out.validateExtendPet(message);
        assertThat(actual).isEqualTo(expected);
    }
}