package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <i>Сервис для сохранения или получания контактных данных пользователя</i>
 */

@Service
@RequiredArgsConstructor
public class BotUserService {
    private final BotUserRepository botUserRepository;

    public void addBotUser(BotUser botUser) {
        botUserRepository.save(botUser);
    }

    public BotUser getBotUser(Long id) {
        return botUserRepository.findBotUserByChatId(id);
    }
}
