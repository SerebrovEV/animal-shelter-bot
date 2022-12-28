package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.exception.BotUserNotFoundException;
import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.repository.BotUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Тесты для проверки работоспособности {@link BotUserService}
 */
@ExtendWith(MockitoExtension.class)
class BotUserServiceTest {

    @InjectMocks
    private BotUserService out;

    @Mock
    BotUserRepository botUserRepository;

    private BotUser BOT_USER1;
    private BotUser BOT_USER2;
    private BotUser BOT_USER3;

    @BeforeEach
    public void setOut() {
        BOT_USER1 = new BotUser("Test", 89871234567L, 123456789L);
        BOT_USER2 = new BotUser("Test2", 89871234568L, 123456781L);
        BOT_USER3 = new BotUser("Test3", 89871234569L, 123456782L);
    }

    @Test
    void addBotUser() {

        when(botUserRepository.save(BOT_USER1)).thenReturn(BOT_USER1);
        when(botUserRepository.save(BOT_USER2)).thenReturn(BOT_USER2);
        when(botUserRepository.save(BOT_USER3)).thenReturn(BOT_USER3);

        BotUser expected = new BotUser("Test", 89871234567L, 123456789L);
        BotUser expected2 = new BotUser("Test2", 89871234568L, 123456781L);
        BotUser expected3 = new BotUser("Test3", 89871234569L, 123456782L);

        BotUser actual = out.addBotUser(BOT_USER1);
        BotUser actual2 = out.addBotUser(BOT_USER2);
        BotUser actual3 = out.addBotUser(BOT_USER3);

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


        when(botUserRepository.findBotUserByChatId(chatId1)).thenReturn(BOT_USER1);
        when(botUserRepository.findBotUserByChatId(chatId2)).thenReturn(BOT_USER2);
        when(botUserRepository.findBotUserByChatId(chatId3)).thenReturn(BOT_USER3);

        BotUser expected = new BotUser("Test", 89871234567L, 123456789L);
        BotUser expected2 = new BotUser("Test2", 89871234568L, 123456781L);
        BotUser expected3 = new BotUser("Test3", 89871234569L, 123456782L);

        BotUser actual = out.getBotUserByChatId(BOT_USER1.getChatId());
        BotUser actual2 = out.getBotUserByChatId(BOT_USER2.getChatId());
        BotUser actual3 = out.getBotUserByChatId(BOT_USER3.getChatId());

        verify(botUserRepository, times(1)).findBotUserByChatId(chatId1);
        verify(botUserRepository, times(1)).findBotUserByChatId(chatId2);
        verify(botUserRepository, times(1)).findBotUserByChatId(chatId3);

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

        when(botUserRepository.findById(botId1)).thenReturn(Optional.ofNullable(BOT_USER1));
        when(botUserRepository.findById(botId2)).thenReturn(Optional.ofNullable(BOT_USER2));
        when(botUserRepository.findById(botId3)).thenReturn(Optional.ofNullable(BOT_USER3));
        when(botUserRepository.findById(4L)).thenReturn(Optional.empty());

        BotUser expected = new BotUser("Test", 89871234567L, 123456789L);
        BotUser expected2 = new BotUser("Test2", 89871234568L, 123456781L);
        BotUser expected3 = new BotUser("Test3", 89871234569L, 123456782L);

        BotUser actual = out.getBotUser(botId1);
        BotUser actual2 = out.getBotUser(botId2);
        BotUser actual3 = out.getBotUser(botId3);

        verify(botUserRepository, times(1)).findById(botId1);
        verify(botUserRepository, times(1)).findById(botId2);
        verify(botUserRepository, times(1)).findById(botId3);

        assertThat(actual.toString()).isEqualTo(expected.toString());
        assertThat(actual2.toString()).isEqualTo(expected2.toString());
        assertThat(actual3.toString()).isEqualTo(expected3.toString());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);

        assertThrows(BotUserNotFoundException.class, () -> out.getBotUser(4L));


    }

    @Test
    void deleteBotUser() {
        Long botId1 = 1L;

        when(botUserRepository.findById(botId1)).thenReturn(Optional.ofNullable(BOT_USER1));
        out.deleteBotUser(botId1);
        verify(botUserRepository, times(1)).findById(botId1);
        assertThrows(BotUserNotFoundException.class, () -> out.deleteBotUser(2L));
    }

    @Test
    void editBotUser() {
        when(botUserRepository.findById(BOT_USER1.getId())).thenReturn(Optional.ofNullable(BOT_USER1));
        when(botUserRepository.save(BOT_USER1)).thenReturn(BOT_USER1);

        BotUser expected = new BotUser("Test", 89871234567L, 123456789L);
        BotUser actual = out.editBotUser(BOT_USER1);
        assertThat(actual.toString()).isEqualTo(expected.toString());

        when(botUserRepository.findById(BOT_USER2.getId())).thenReturn(Optional.empty());
        assertThrows(BotUserNotFoundException.class, () -> out.editBotUser(BOT_USER2));
    }

    @Test
    void getAll() {
        when(botUserRepository.findAll()).thenReturn(new ArrayList<>(List.of(
                BOT_USER1,
                BOT_USER2,
                BOT_USER3)));
        List<BotUser> expected = new ArrayList<>(List.of(
                new BotUser("Test", 89871234567L, 123456789L),
                new BotUser("Test2", 89871234568L, 123456781L),
                new BotUser("Test3", 89871234569L, 123456782L)));
        List<BotUser> actual = out.getAll();

        assertThat(actual).isEqualTo(expected);
    }
}