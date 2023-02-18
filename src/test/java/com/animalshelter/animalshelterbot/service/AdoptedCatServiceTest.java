package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
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

    private AdoptedCat cat;
    private AdoptedCat cat2;
    private AdoptedCat cat3;

    @BeforeEach
    public void setOut() {
        cat = new AdoptedCat("Test");
        cat2 = new AdoptedCat("Test2");
        cat3 = new AdoptedCat("Test3");
    }

    @Test
    void addAdoptedCat() {
        when(adoptedCatRepository.save(cat)).thenReturn(cat);
        when(adoptedCatRepository.save(cat2)).thenReturn(cat2);
        when(adoptedCatRepository.save(cat3)).thenReturn(cat3);

        AdoptedCat expected = new AdoptedCat("Test");
        AdoptedCat expected2 = new AdoptedCat("Test2");
        AdoptedCat expected3 = new AdoptedCat("Test3");

        AdoptedCat actual = out.addAdoptedCat(cat);
        AdoptedCat actual2 = out.addAdoptedCat(cat2);
        AdoptedCat actual3 = out.addAdoptedCat(cat3);

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

        when(adoptedCatRepository.findById(catId1)).thenReturn(Optional.ofNullable(cat));
        when(adoptedCatRepository.findById(catId2)).thenReturn(Optional.ofNullable(cat2));
        when(adoptedCatRepository.findById(catId3)).thenReturn(Optional.ofNullable(cat3));


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
        when(adoptedCatRepository.save(cat)).thenReturn(cat);
        AdoptedCat expected = new AdoptedCat("Test");
        AdoptedCat actual = out.editAdoptedCat(cat);
        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllCat() {
        when(adoptedCatRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                cat,
                cat2,
                cat3)));
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
                cat,
                cat2,
                cat3)));
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
                cat,
                cat2,
                cat3)));
        List<AdoptedCat> expected = new ArrayList<>(List.of(
                new AdoptedCat("Test"),
                new AdoptedCat("Test2"),
                new AdoptedCat("Test3")));
        List<AdoptedCat> actual = out.getAllFreeCat();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllCatWithEndPeriod() {

        cat.setAdoptionDate(Date.valueOf(LocalDate.of(1990, 8, 1)));
        cat2.setAdoptionDate(Date.valueOf(LocalDate.of(1990, 8, 1)));
        cat3.setAdoptionDate(Date.valueOf(LocalDate.of(1990, 8, 1)));


        cat.setTrialPeriod(30);
        cat2.setTrialPeriod(30);
        cat3.setTrialPeriod(30);

        cat.setCatUser(new CatUser());
        cat2.setCatUser(new CatUser());
        cat3.setCatUser(new CatUser());

        List<AdoptedCat> cats = new ArrayList<>(List.of(
                cat,
                cat2,
                cat3));

        when(adoptedCatRepository.findAllByCatUserNotNull()).thenReturn(cats);

        List<AdoptedCat> expected = new ArrayList<>(List.of(
                cat,
                cat2,
                cat3));

        List<AdoptedCat> actual = out.getAllCatWithEndPeriod();

        assertThat(actual).isEqualTo(expected);
    }
}