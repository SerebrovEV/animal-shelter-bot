package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.repository.AdoptedDogRepository;
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

@ExtendWith(MockitoExtension.class)
class AdoptedDogServiceTest {

    @InjectMocks
    AdoptedDogService adoptedDogService;
    @Mock
    AdoptedDogRepository adoptedDogRepository;
    private AdoptedDog D1;
    private AdoptedDog D2;
    private AdoptedDog D3;

    @BeforeEach
    public void setOut() {
        D1 = new AdoptedDog("C1");
        D2 = new AdoptedDog("C2");
        D3 = new AdoptedDog("C3");
    }

    @Test
    void addAdoptedDog() {
        when(adoptedDogRepository.save(D1)).thenReturn(D1);
        when(adoptedDogRepository.save(D2)).thenReturn(D2);
        when(adoptedDogRepository.save(D3)).thenReturn(D3);

        AdoptedDog expected = new AdoptedDog("C1");
        AdoptedDog expected1 = new AdoptedDog("C2");
        AdoptedDog expected2 = new AdoptedDog("C3");

        AdoptedDog actual = adoptedDogService.addAdoptedDog(D1);
        AdoptedDog actual1 = adoptedDogService.addAdoptedDog(D2);
        AdoptedDog actual2 = adoptedDogService.addAdoptedDog(D3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual1.toString()).isEqualTo(expected1.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
    }
    @Test
    void deleteAdoptedDog() {
        Long dogId1 = 1L;
        adoptedDogService.deleteAdoptedDog(dogId1);
        verify(adoptedDogRepository, times(1)).deleteById(dogId1);

    }

    @Test
    void getAdoptedDog() {
        Long dogId1 = 1L;
        Long dogId2 = 2L;
        Long dogId3 = 3L;

        when(adoptedDogRepository.findById(dogId1)).thenReturn(Optional.ofNullable(D1));
        when(adoptedDogRepository.findById(dogId2)).thenReturn(Optional.ofNullable(D2));
        when(adoptedDogRepository.findById(dogId3)).thenReturn(Optional.ofNullable(D3));

        AdoptedDog expected = new AdoptedDog("C1");
        AdoptedDog expected1 = new AdoptedDog("C2");
        AdoptedDog expected2 = new AdoptedDog("C3");

        AdoptedDog actual = adoptedDogService.getAdoptedDog(dogId1).get();
        AdoptedDog actual1 = adoptedDogService.getAdoptedDog(dogId2).get();
        AdoptedDog actual2 = adoptedDogService.getAdoptedDog(dogId3).get();

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual1.toString()).isEqualTo(expected1.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual1).isEqualTo(expected1);
        assertThat(actual2).isEqualTo(expected2);
    }

    @Test
    void editAdoptedDog() {
        when(adoptedDogRepository.save(D1)).thenReturn(D1);
        AdoptedDog expected = new AdoptedDog("C1");
        AdoptedDog actual = adoptedDogService.editAdoptedDog(D1);
        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllDog() {
        when(adoptedDogRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                D1,
                D2,
                D3)));
        List<AdoptedDog> expected = new ArrayList<>(List.of(
                new AdoptedDog("C1"),
                new AdoptedDog("C2"),
                new AdoptedDog("C3")));
        List<AdoptedDog> actual = adoptedDogService.getAllDog();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllFreeDog() {
        when(adoptedDogRepository.findAllByDogUserIsNull()).thenReturn(new ArrayList<>(List.of(
                D1,
                D2,
                D3)));
        List<AdoptedDog> expected = new ArrayList<>(List.of(
                new AdoptedDog("C1"),
                new AdoptedDog("C2"),
                new AdoptedDog("C3")));
        List<AdoptedDog> actual = adoptedDogService.getAllFreeDog();
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    void getAllDogOnTrialPeriod() {
        when(adoptedDogRepository.findAllByDogUserIsNotNull()).thenReturn(new ArrayList<>(List.of(
                D1,
                D2,
                D3)));
        List<AdoptedDog> expected = new ArrayList<>(List.of(
                new AdoptedDog("C1"),
                new AdoptedDog("C2"),
                new AdoptedDog("C3")));
        List<AdoptedDog> actual = adoptedDogService.getAllDogOnTrialPeriod();
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void getAllDogWithEndPeriod() {

        D1.setAdoptionDate(Date.valueOf(LocalDate.of(2022, 1, 18)));
        D2.setAdoptionDate(Date.valueOf(LocalDate.of(2022, 1, 18)));
        D3.setAdoptionDate(Date.valueOf(LocalDate.of(2022, 1, 18)));

        D1.setTrialPeriod(30);
        D2.setTrialPeriod(30);
        D3.setTrialPeriod(30);

        D1.setDogUser(new DogUser());
        D2.setDogUser(new DogUser());
        D3.setDogUser(new DogUser());

        List<AdoptedDog> dogs = new ArrayList<>(List.of( D1, D2, D3));

        when(adoptedDogRepository.findAllByDogUserIsNotNull()).thenReturn(dogs);

        List<AdoptedDog> expected = new ArrayList<>(List.of( D1, D2, D3));

        List<AdoptedDog> actual = adoptedDogService.getAllDogWithEndPeriod();

        assertThat(actual).isEqualTo(expected);
    }

}