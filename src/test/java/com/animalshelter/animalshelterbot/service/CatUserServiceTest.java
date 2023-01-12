package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.repository.CatUserRepository;
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

@ExtendWith(MockitoExtension.class)
class CatUserServiceTest {

    @InjectMocks
    private CatUserService out;

    @Mock
    CatUserRepository catUserRepository;

    private CatUser BOT_USER1;
    private CatUser BOT_USER2;
    private CatUser BOT_USER3;

    @BeforeEach
    public void setOut() {
        BOT_USER1 = new CatUser("Test", 89871234567L, 123456789L);
        BOT_USER2 = new CatUser("Test2", 89871234568L, 123456781L);
        BOT_USER3 = new CatUser("Test3", 89871234569L, 123456782L);
    }

    @Test
    void addCatUser() {
        when(catUserRepository.save(BOT_USER1)).thenReturn(BOT_USER1);
        when(catUserRepository.save(BOT_USER2)).thenReturn(BOT_USER2);
        when(catUserRepository.save(BOT_USER3)).thenReturn(BOT_USER3);

        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser expected2 = new CatUser("Test2", 89871234568L, 123456781L);
        CatUser expected3 = new CatUser("Test3", 89871234569L, 123456782L);

        CatUser actual = out.addCatUser(BOT_USER1);
        CatUser actual2 = out.addCatUser(BOT_USER2);
        CatUser actual3 = out.addCatUser(BOT_USER3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
    }

    @Test
    void getCatUserByChatId() {
        Long chatId1 = 123456789L;
        Long chatId2 = 123456781L;
        Long chatId3 = 123456782L;


        when(catUserRepository.findCatUserByChatId(chatId1)).thenReturn(BOT_USER1);
        when(catUserRepository.findCatUserByChatId(chatId2)).thenReturn(BOT_USER2);
        when(catUserRepository.findCatUserByChatId(chatId3)).thenReturn(BOT_USER3);

        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser expected2 = new CatUser("Test2", 89871234568L, 123456781L);
        CatUser expected3 = new CatUser("Test3", 89871234569L, 123456782L);

        CatUser actual = out.getCatUserByChatId(BOT_USER1.getChatId());
        CatUser actual2 = out.getCatUserByChatId(BOT_USER2.getChatId());
        CatUser actual3 = out.getCatUserByChatId(BOT_USER3.getChatId());

        verify(catUserRepository, times(1)).findCatUserByChatId(chatId1);
        verify(catUserRepository, times(1)).findCatUserByChatId(chatId2);
        verify(catUserRepository, times(1)).findCatUserByChatId(chatId3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
    }

    @Test
    void getCatUserByPhoneNumber() {
        when(catUserRepository.findByPhoneNumber(anyLong())).thenReturn(BOT_USER1);
        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser actual = out.getCatUserByPhoneNumber(BOT_USER1.getPhoneNumber());
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    void editCatUser() {
        when(catUserRepository.save(BOT_USER1)).thenReturn(BOT_USER1);
        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser actual = out.editCatUser(BOT_USER1);
        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getCatUser() {
        Long botId1 = 1L;
        Long botId2 = 2L;
        Long botId3 = 3L;

        when(catUserRepository.findById(botId1)).thenReturn(Optional.ofNullable(BOT_USER1));
        when(catUserRepository.findById(botId2)).thenReturn(Optional.ofNullable(BOT_USER2));
        when(catUserRepository.findById(botId3)).thenReturn(Optional.ofNullable(BOT_USER3));


        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser expected2 = new CatUser("Test2", 89871234568L, 123456781L);
        CatUser expected3 = new CatUser("Test3", 89871234569L, 123456782L);

        CatUser actual = out.getCatUser(botId1).get();
        CatUser actual2 = out.getCatUser(botId2).get();
        CatUser actual3 = out.getCatUser(botId3).get();

        verify(catUserRepository, times(1)).findById(botId1);
        verify(catUserRepository, times(1)).findById(botId2);
        verify(catUserRepository, times(1)).findById(botId3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
    }

    @Test
    void deleteCatUser() {
        Long botId1 = 1L;
        out.deleteCatUser(botId1);
        verify(catUserRepository, times(1)).deleteById(botId1);

    }

    @Test
    void getAllCatUser() {
        when(catUserRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                BOT_USER1,
                BOT_USER2,
                BOT_USER3)));
        List<CatUser> expected = new ArrayList<>(List.of(
                new CatUser("Test", 89871234567L, 123456789L),
                new CatUser("Test2", 89871234568L, 123456781L),
                new CatUser("Test3", 89871234569L, 123456782L)));
        List<CatUser> actual = out.getAllCatUser();

        assertThat(actual).isEqualTo(expected);
    }
}