package com.animalshelter.animalshelterbot.service;


import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <i>Сервис для добавления, получения, редактирования и удаления контактных данных усыновителей
 * {@link BotUser} в/из базы данных</i>
 */

@Service
@RequiredArgsConstructor
public class BotUserService {
    private final BotUserRepository botUserRepository;

    public BotUser addBotUser(BotUser botUser) {
        return botUserRepository.save(botUser);
    }

    public BotUser getBotUserByChatId(Long chatId) {
        return botUserRepository.findBotUserByChatId(chatId);
    }

    public Optional<BotUser> getBotUser(Long id) {
        Optional<BotUser> findBotUser = botUserRepository.findById(id);
        return findBotUser;
    }

    public void deleteBotUser(Long id) {
        botUserRepository.deleteById(id);
    }

    public BotUser getByPhoneNumber(Long phoneNumber){
        return botUserRepository.findByPhoneNumber(phoneNumber);
    }

    public BotUser editBotUser(BotUser botUser) {
        return botUserRepository.save(botUser);
    }

    public List<BotUser> getAll() {
        return List.copyOf(botUserRepository.findAll());
    }

}
