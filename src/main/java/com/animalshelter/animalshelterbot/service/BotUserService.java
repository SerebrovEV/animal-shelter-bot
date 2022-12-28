package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.exception.BotUserNotFoundException;
import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <i>Сервис для добавления, получения, редактирования и удаления контактных данных пользователя
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

    public BotUser getBotUser(Long id) {
        Optional<BotUser> getBotUser = botUserRepository.findById(id);
        if (getBotUser.isEmpty()) {
            throw new BotUserNotFoundException();
        }
        return getBotUser.get();
    }

    public void deleteBotUser(Long id) {
        BotUser botUserForDelete = getBotUser(id);
        botUserRepository.deleteById(botUserForDelete.getId());
    }

    public BotUser editBotUser(BotUser botUser) {
        BotUser editBotUser = getBotUser(botUser.getId());
        editBotUser.setUserName(botUser.getUserName());
        editBotUser.setChatId(botUser.getChatId());
        editBotUser.setPhoneNumber(botUser.getPhoneNumber());
        return botUserRepository.save(editBotUser);
    }

    public List<BotUser> getAll() {
        return List.copyOf(botUserRepository.findAll());
    }

}
