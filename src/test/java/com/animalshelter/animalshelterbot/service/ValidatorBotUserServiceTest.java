package com.animalshelter.animalshelterbot.service;

import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ValidatorBotUserServiceTest {
    @InjectMocks
    private ValidatorBotUserService out;

    @Mock
    private BotUserService botUserService;

    @Mock
    Message message;

    @Test
    void validateUser() {

    }

    @Test
    void validateGetUser(){

    }
}