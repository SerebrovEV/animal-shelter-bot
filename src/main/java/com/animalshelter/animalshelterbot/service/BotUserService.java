package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotUserService {
    private final BotUserRepository botUserRepository;

    public void addBotUser(BotUser botUser) {
        botUserRepository.save(botUser);
    }

    public String getBotUser(Long id) {
        BotUser botUser = botUserRepository.findBotUserByUserId(id);
        if (botUser != null) {
            return botUser.toString();
        }
        return "Клиент не найден! Пожалуйста добавте контакты для обратной связи или запросите вызов волентера. Спасибо!";
    }
}
