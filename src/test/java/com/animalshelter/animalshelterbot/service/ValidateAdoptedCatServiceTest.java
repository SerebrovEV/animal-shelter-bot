package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ValidateAdoptedCatServiceTest {
    @InjectMocks
    ValidateAdoptedCatService out;

    @Mock
    AdoptedCatService adoptedCatService;

 @Mock
 Message message;

    @Test
    void validateAddCat() {

            AdoptedCat adoptedCat = new AdoptedCat("Тест");

            when(message.text()).thenReturn("Сохранить к Тест");
            when(adoptedCatService.addAdoptedCat(any())).thenReturn(adoptedCat);

            String expected ="Добавлена запись кота в базу данных приюта для кошек: " + adoptedCat.getCatName();
            String actual = out.validateAddCat(message);
            assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateAddCatIncorrectText() {

        when(message.text()).thenReturn("Сохранить к");
        String expected ="Некорректный запрос";
        String actual = out.validateAddCat(message);
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    void validateDeleteCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");

        when(message.text()).thenReturn("Удалить к 1");
        when(adoptedCatService.getAdoptedCat(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected =adoptedCat + " удалена из базы данных приюта для кошек.";
        String actual = out.validateDeleteCat(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateDeleteCatNotFound() {

        when(message.text()).thenReturn("Удалить к 1");
        when(adoptedCatService.getAdoptedCat(anyLong())).thenReturn(Optional.empty());

        String expected ="Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateDeleteCat(message);
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    void validateDeleteCatIncorrectText() {

        when(message.text()).thenReturn("Удалить к");
        String expected ="Некорректный запрос";
        String actual = out.validateDeleteCat(message);
        assertThat(actual).isEqualTo(expected);

    }


    @Test
    void validateGetCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");

        when(message.text()).thenReturn("Найти к 1");
        when(adoptedCatService.getAdoptedCat(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected =adoptedCat + " из базы данных приюта для кошек.";
        String actual = out.validateGetCat(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateGetCatNotFound() {

        when(message.text()).thenReturn("Найти к 1");
        when(adoptedCatService.getAdoptedCat(anyLong())).thenReturn(Optional.empty());

        String expected ="Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateGetCat(message);
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    void validateGetCatIncorrectText() {

        when(message.text()).thenReturn("Удалить к");
        String expected ="Некорректный запрос";
        String actual = out.validateGetCat(message);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void validateEditCat() {
        AdoptedCat adoptedCat = new AdoptedCat("Тест");
        adoptedCat.setId(1L);

        when(message.text()).thenReturn("Изменить к 1 Тест2");
        when(adoptedCatService.getAdoptedCat(anyLong())).thenReturn(Optional.of(adoptedCat));

        String expected =adoptedCat + " изменен в базе данных приюта для кошек.";
        String actual = out.validateEditCat(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditCatNotFound() {

        when(message.text()).thenReturn("Изменить к 1 Тест2");
        when(adoptedCatService.getAdoptedCat(anyLong())).thenReturn(Optional.empty());

        String expected ="Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
        String actual = out.validateEditCat(message);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateEditCatIncorrectText() {

        when(message.text()).thenReturn("Изменить к");

        String expected ="Некорректный запрос";;
        String actual = out.validateEditCat(message);
        assertThat(actual).isEqualTo(expected);
    }
}