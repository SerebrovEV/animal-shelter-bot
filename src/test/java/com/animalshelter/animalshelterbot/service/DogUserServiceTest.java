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

    private DogUser BOT_USER1;
    private DogUser BOT_USER2;
    private DogUser BOT_USER3;

    @BeforeEach
    public void setOut() {
        BOT_USER1 = new DogUser("Test", 89871234567L, 123456789L);
        BOT_USER2 = new DogUser("Test2", 89871234568L, 123456781L);
        BOT_USER3 = new DogUser("Test3", 89871234569L, 123456782L);
    }

    @Test
    void addBotUser() {

        when(dogUserRepository.save(BOT_USER1)).thenReturn(BOT_USER1);
        when(dogUserRepository.save(BOT_USER2)).thenReturn(BOT_USER2);
        when(dogUserRepository.save(BOT_USER3)).thenReturn(BOT_USER3);

        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser expected2 = new DogUser("Test2", 89871234568L, 123456781L);
        DogUser expected3 = new DogUser("Test3", 89871234569L, 123456782L);

        DogUser actual = out.addDogUser(BOT_USER1);
        DogUser actual2 = out.addDogUser(BOT_USER2);
        DogUser actual3 = out.addDogUser(BOT_USER3);

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


        when(dogUserRepository.findBotUserByChatId(chatId1)).thenReturn(BOT_USER1);
        when(dogUserRepository.findBotUserByChatId(chatId2)).thenReturn(BOT_USER2);
        when(dogUserRepository.findBotUserByChatId(chatId3)).thenReturn(BOT_USER3);

        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser expected2 = new DogUser("Test2", 89871234568L, 123456781L);
        DogUser expected3 = new DogUser("Test3", 89871234569L, 123456782L);

        DogUser actual = out.getDogUserByChatId(BOT_USER1.getChatId());
        DogUser actual2 = out.getDogUserByChatId(BOT_USER2.getChatId());
        DogUser actual3 = out.getDogUserByChatId(BOT_USER3.getChatId());

        verify(dogUserRepository, times(1)).findBotUserByChatId(chatId1);
        verify(dogUserRepository, times(1)).findBotUserByChatId(chatId2);
        verify(dogUserRepository, times(1)).findBotUserByChatId(chatId3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);

    }

    @Test
    void getBotUser() {
        Long botId1 = 1L;
        Long botId2 = 2L;
        Long botId3 = 3L;

        when(dogUserRepository.findById(botId1)).thenReturn(Optional.ofNullable(BOT_USER1));
        when(dogUserRepository.findById(botId2)).thenReturn(Optional.ofNullable(BOT_USER2));
        when(dogUserRepository.findById(botId3)).thenReturn(Optional.ofNullable(BOT_USER3));


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
    void deleteBotUser() {
        Long botId1 = 1L;
        out.deleteDogUser(botId1);
        verify(dogUserRepository, times(1)).deleteById(botId1);

    }

    @Test
    void getByPhoneNumber() {
        when(dogUserRepository.findByPhoneNumber(anyLong())).thenReturn(BOT_USER1);
        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser actual = out.getByPhoneNumber(BOT_USER1.getPhoneNumber());
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    void editBotUser() {

        when(dogUserRepository.save(BOT_USER1)).thenReturn(BOT_USER1);
        DogUser expected = new DogUser("Test", 89871234567L, 123456789L);
        DogUser actual = out.editDogUser(BOT_USER1);
        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAll() {
        when(dogUserRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                BOT_USER1,
                BOT_USER2,
                BOT_USER3)));
        List<DogUser> expected = new ArrayList<>(List.of(
                new DogUser("Test", 89871234567L, 123456789L),
                new DogUser("Test2", 89871234568L, 123456781L),
                new DogUser("Test3", 89871234569L, 123456782L)));
        List<DogUser> actual = out.getAllDogUser();

        assertThat(actual).isEqualTo(expected);
    }
}