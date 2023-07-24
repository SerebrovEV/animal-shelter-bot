package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.repository.CatUserRepository;
import com.animalshelter.animalshelterbot.service.impl.CatUserService;
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
 * Тесты для проверки работоспособности {@link CatUserService}
 */
@ExtendWith(MockitoExtension.class)
class CatUserServiceTest {

    @InjectMocks
    private CatUserService out;

    @Mock
    CatUserRepository catUserRepository;

    private CatUser botUser1;
    private CatUser botUser2;
    private CatUser botUser3;

    @BeforeEach
    public void setOut() {
        botUser1 = new CatUser("Test", 89871234567L, 123456789L);
        botUser2 = new CatUser("Test2", 89871234568L, 123456781L);
        botUser3 = new CatUser("Test3", 89871234569L, 123456782L);
    }

    @Test
    void addCatUser() {
        when(catUserRepository.save(botUser1)).thenReturn(botUser1);
        when(catUserRepository.save(botUser2)).thenReturn(botUser2);
        when(catUserRepository.save(botUser3)).thenReturn(botUser3);

        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser expected2 = new CatUser("Test2", 89871234568L, 123456781L);
        CatUser expected3 = new CatUser("Test3", 89871234569L, 123456782L);

        CatUser actual = out.addUser(botUser1);
        CatUser actual2 = out.addUser(botUser2);
        CatUser actual3 = out.addUser(botUser3);

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


        when(catUserRepository.findCatUserByChatId(chatId1)).thenReturn(Optional.ofNullable(botUser1));
        when(catUserRepository.findCatUserByChatId(chatId2)).thenReturn(Optional.ofNullable(botUser2));
        when(catUserRepository.findCatUserByChatId(chatId3)).thenReturn(Optional.ofNullable(botUser3));

        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser expected2 = new CatUser("Test2", 89871234568L, 123456781L);
        CatUser expected3 = new CatUser("Test3", 89871234569L, 123456782L);

        CatUser actual = out.getUserByChatId(botUser1.getChatId()).get();
        CatUser actual2 = out.getUserByChatId(botUser2.getChatId()).get();
        CatUser actual3 = out.getUserByChatId(botUser3.getChatId()).get();

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
        when(catUserRepository.findByPhoneNumber(anyLong())).thenReturn(Optional.ofNullable(botUser1));
        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser actual = out.getUserByPhoneNumber(botUser1.getPhoneNumber()).get();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    void editCatUser() {
        when(catUserRepository.save(botUser1)).thenReturn(botUser1);
        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser actual = out.editUser(botUser1);
        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getCatUser() {
        Long botId1 = 1L;
        Long botId2 = 2L;
        Long botId3 = 3L;

        when(catUserRepository.findById(botId1)).thenReturn(Optional.ofNullable(botUser1));
        when(catUserRepository.findById(botId2)).thenReturn(Optional.ofNullable(botUser2));
        when(catUserRepository.findById(botId3)).thenReturn(Optional.ofNullable(botUser3));


        CatUser expected = new CatUser("Test", 89871234567L, 123456789L);
        CatUser expected2 = new CatUser("Test2", 89871234568L, 123456781L);
        CatUser expected3 = new CatUser("Test3", 89871234569L, 123456782L);

        CatUser actual = out.getUser(botId1).get();
        CatUser actual2 = out.getUser(botId2).get();
        CatUser actual3 = out.getUser(botId3).get();

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
        out.deleteUser(botId1);
        verify(catUserRepository, times(1)).deleteById(botId1);

    }

    @Test
    void getAllCatUser() {
        when(catUserRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                botUser1,
                botUser2,
                botUser3)));
        List<CatUser> expected = new ArrayList<>(List.of(
                new CatUser("Test", 89871234567L, 123456789L),
                new CatUser("Test2", 89871234568L, 123456781L),
                new CatUser("Test3", 89871234569L, 123456782L)));
        List<CatUser> actual = out.getAllUser();

        assertThat(actual).isEqualTo(expected);
    }
}