package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.repository.DogUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * Тесты для проверки работоспособности {@link DogUserService}
 */
@ExtendWith(MockitoExtension.class)
class DogUserServiceTest {

    @InjectMocks
    private DogUserService out;

    @Mock
    DogUserRepository dogUserRepository;

    private DogUser botUser1;
    private DogUser botUser2;
    private DogUser botUser3;

    @BeforeEach
    public void setOut() {
        botUser1 = new DogUser("Test", 89871234567L, 123456789L);
        botUser2 = new DogUser("Test2", 89871234568L, 123456781L);
        botUser3 = new DogUser("Test3", 89871234569L, 123456782L);
    }

    @Test
    void addDogUser() {

        when(dogUserRepository.save(botUser1)).thenReturn(botUser1);
        when(dogUserRepository.save(botUser2)).thenReturn(botUser2);
        when(dogUserRepository.save(botUser3)).thenReturn(botUser3);

        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser expected2 = new DogUser("Test2", 89871234568L, 123456781L);
        DogUser expected3 = new DogUser("Test3", 89871234569L, 123456782L);

        DogUser actual = out.addDogUser(botUser1);
        DogUser actual2 = out.addDogUser(botUser2);
        DogUser actual3 = out.addDogUser(botUser3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
    }

    @Test
    void getBotUserByChatId() {
        Long chatId1 = 123456789L;
        Long chatId2 = 123456781L;
        Long chatId3 = 123456782L;


        when(dogUserRepository.findDogUserByChatId(chatId1)).thenReturn(Optional.ofNullable(botUser1));
        when(dogUserRepository.findDogUserByChatId(chatId2)).thenReturn(Optional.ofNullable(botUser2));
        when(dogUserRepository.findDogUserByChatId(chatId3)).thenReturn(Optional.ofNullable(botUser3));

        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser expected2 = new DogUser("Test2", 89871234568L, 123456781L);
        DogUser expected3 = new DogUser("Test3", 89871234569L, 123456782L);

        DogUser actual = out.getDogUserByChatId(botUser1.getChatId()).get();
        DogUser actual2 = out.getDogUserByChatId(botUser2.getChatId()).get();
        DogUser actual3 = out.getDogUserByChatId(botUser3.getChatId()).get();

        verify(dogUserRepository, times(1)).findDogUserByChatId(chatId1);
        verify(dogUserRepository, times(1)).findDogUserByChatId(chatId2);
        verify(dogUserRepository, times(1)).findDogUserByChatId(chatId3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
    }
    @Test
    void getDogUserByPhoneNumber() {
        when(dogUserRepository.findDogUserByPhoneNumber(anyLong())).thenReturn(Optional.ofNullable(botUser1));
        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser actual = out.getDogUserByPhoneNumber(botUser1.getPhoneNumber()).get();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    void getDogUser() {
        Long botId1 = 1L;
        Long botId2 = 2L;
        Long botId3 = 3L;

        when(dogUserRepository.findById(botId1)).thenReturn(Optional.ofNullable(botUser1));
        when(dogUserRepository.findById(botId2)).thenReturn(Optional.ofNullable(botUser2));
        when(dogUserRepository.findById(botId3)).thenReturn(Optional.ofNullable(botUser3));


        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser expected2 = new DogUser("Test2", 89871234568L, 123456781L);
        DogUser expected3 = new DogUser("Test3", 89871234569L, 123456782L);

        DogUser actual = out.getDogUser(botId1).get();
        DogUser actual2 = out.getDogUser(botId2).get();
        DogUser actual3 = out.getDogUser(botId3).get();

        verify(dogUserRepository, times(1)).findById(botId1);
        verify(dogUserRepository, times(1)).findById(botId2);
        verify(dogUserRepository, times(1)).findById(botId3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);

    }

    @Test
    void deleteDogUser() {
        Long botId1 = 1L;
        out.deleteDogUser(botId1);
        verify(dogUserRepository, times(1)).deleteById(botId1);

    }
    @Test
    void editDogUser() {

        when(dogUserRepository.save(botUser1)).thenReturn(botUser1);
        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser actual = out.editDogUser(botUser1);
        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllDogUser() {
        when(dogUserRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                botUser1,
                botUser2,
                botUser3)));
        List<DogUser> expected = new ArrayList<>(List.of(
                new DogUser("Test", 89871234567L, 123456789L),
                new DogUser("Test2", 89871234568L, 123456781L),
                new DogUser("Test3", 89871234569L, 123456782L)));
        List<DogUser> actual = out.getAllDogUser();

        assertThat(actual).isEqualTo(expected);
    }
}