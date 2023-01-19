package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.controllers.AdminCatController;
import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import liquibase.pro.packaged.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Тесты для проверки работоспособности {@link AdoptedCatService}
 */
@ExtendWith(MockitoExtension.class)
class AdoptedCatServiceTest {

    @InjectMocks
    AdoptedCatService out;
    @Mock
    AdoptedCatRepository adoptedCatRepository;

    private AdoptedCat CAT1;
    private AdoptedCat CAT2;
    private AdoptedCat CAT3;

    @BeforeEach
    public void setOut() {
        CAT1 = new AdoptedCat("Test");
        CAT2 = new AdoptedCat("Test2");
        CAT3 = new AdoptedCat("Test3");
    }

    @Test
    void addAdoptedCat() {
        when(adoptedCatRepository.save(CAT1)).thenReturn(CAT1);
        when(adoptedCatRepository.save(CAT2)).thenReturn(CAT2);
        when(adoptedCatRepository.save(CAT3)).thenReturn(CAT3);

        AdoptedCat expected = new AdoptedCat("Test");
        AdoptedCat expected2 = new AdoptedCat("Test2");
        AdoptedCat expected3 = new AdoptedCat("Test3");

        AdoptedCat actual = out.addAdoptedCat(CAT1);
        AdoptedCat actual2 = out.addAdoptedCat(CAT2);
        AdoptedCat actual3 = out.addAdoptedCat(CAT3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
    }

    @Test
    void deleteAdoptedCat() {
        Long catId1 = 1L;
        out.deleteAdoptedCat(catId1);
        verify(adoptedCatRepository, times(1)).deleteById(catId1);
    }

    @Test
    void getAdoptedCat() {
        Long catId1 = 1L;
        Long catId2 = 2L;
        Long catId3 = 3L;

        when(adoptedCatRepository.findById(catId1)).thenReturn(Optional.ofNullable(CAT1));
        when(adoptedCatRepository.findById(catId2)).thenReturn(Optional.ofNullable(CAT2));
        when(adoptedCatRepository.findById(catId3)).thenReturn(Optional.ofNullable(CAT3));


        AdoptedCat expected = new AdoptedCat("Test");
        AdoptedCat expected2 = new AdoptedCat("Test2");
        AdoptedCat expected3 = new AdoptedCat("Test3");

        AdoptedCat actual = out.getAdoptedCat(catId1).get();
        AdoptedCat actual2 = out.getAdoptedCat(catId2).get();
        AdoptedCat actual3 = out.getAdoptedCat(catId3).get();

        verify(adoptedCatRepository, times(1)).findById(catId1);
        verify(adoptedCatRepository, times(1)).findById(catId2);
        verify(adoptedCatRepository, times(1)).findById(catId3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
    }

    @Test
    void editAdoptedCat() {
        when(adoptedCatRepository.save(CAT1)).thenReturn(CAT1);
        AdoptedCat expected = new AdoptedCat("Test");
        AdoptedCat actual = out.editAdoptedCat(CAT1);
        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllCat() {
        when(adoptedCatRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                CAT1,
                CAT2,
                CAT3)));
        List<AdoptedCat> expected = new ArrayList<>(List.of(
                new AdoptedCat("Test"),
                new AdoptedCat("Test2"),
                new AdoptedCat("Test3")));
        List<AdoptedCat> actual = out.getAllCat();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllBusyCat() {
        when(adoptedCatRepository.findAllByCatUserNotNull()).thenReturn(new ArrayList<>(List.of(
                CAT1,
                CAT2,
                CAT3)));
        List<AdoptedCat> expected = new ArrayList<>(List.of(
                new AdoptedCat("Test"),
                new AdoptedCat("Test2"),
                new AdoptedCat("Test3")));
        List<AdoptedCat> actual = out.getAllBusyCat();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllFreeCat() {
        when(adoptedCatRepository.findAllByCatUserIsNull()).thenReturn(new ArrayList<>(List.of(
                CAT1,
                CAT2,
                CAT3)));
        List<AdoptedCat> expected = new ArrayList<>(List.of(
                new AdoptedCat("Test"),
                new AdoptedCat("Test2"),
                new AdoptedCat("Test3")));
        List<AdoptedCat> actual = out.getAllFreeCat();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllCatWithEndPeriod() {

        CAT1.setAdoptionDate(Date.valueOf(LocalDate.of(1990, 8, 1)));
        CAT2.setAdoptionDate(Date.valueOf(LocalDate.of(1990, 8, 1)));
        CAT3.setAdoptionDate(Date.valueOf(LocalDate.of(1990, 8, 1)));


        CAT1.setTrialPeriod(30);
        CAT2.setTrialPeriod(30);
        CAT3.setTrialPeriod(30);

        CAT1.setCatUser(new CatUser());
        CAT2.setCatUser(new CatUser());
        CAT3.setCatUser(new CatUser());

        List<AdoptedCat> cats = new ArrayList<>(List.of(
                CAT1,
                CAT2,
                CAT3));

        when(adoptedCatRepository.findAllByCatUserNotNull()).thenReturn(cats);

        List<AdoptedCat> expected = new ArrayList<>(List.of(
                CAT1,
                CAT2,
                CAT3));

        List<AdoptedCat> actual = out.getAllCatWithEndPeriod();

        assertThat(actual).isEqualTo(expected);
    }
}