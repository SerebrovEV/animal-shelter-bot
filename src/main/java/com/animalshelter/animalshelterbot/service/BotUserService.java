package com.animalshelter.animalshelterbot.service;


import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.repository.DogUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <i>Сервис для добавления, получения, редактирования и удаления контактных данных усыновителей
 * {@link DogUser} в/из базы данных</i>
 */

@Service
@RequiredArgsConstructor
public class BotUserService {
    private final DogUserRepository dogUserRepository;

    public DogUser addBotUser(DogUser dogUser) {
        return dogUserRepository.save(dogUser);
    }

    public DogUser getBotUserByChatId(Long chatId) {
        return dogUserRepository.findBotUserByChatId(chatId);
    }

    public Optional<DogUser> getBotUser(Long id) {
        Optional<DogUser> findBotUser = dogUserRepository.findById(id);
        return findBotUser;
    }

    public void deleteBotUser(Long id) {
        dogUserRepository.deleteById(id);
    }

    public DogUser getByPhoneNumber(Long phoneNumber){
        return dogUserRepository.findByPhoneNumber(phoneNumber);
    }

    public DogUser editBotUser(DogUser dogUser) {
        return dogUserRepository.save(dogUser);
    }

    public List<DogUser> getAll() {
        return List.copyOf(dogUserRepository.findAll());
    }

}
