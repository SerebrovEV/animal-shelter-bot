package com.animalshelter.animalshelterbot.service;


import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.repository.DogUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <i>Сервис для добавления, получения, редактирования и удаления контактных данных усыновителей
 * {@link DogUser} в/из базы данных собачего приюта</i>
 */

@Service
@RequiredArgsConstructor
public class DogUserService {
    private final DogUserRepository dogUserRepository;

    public DogUser addDogUser(DogUser dogUser) {
        return dogUserRepository.save(dogUser);
    }

    public DogUser getDogUserByChatId(Long chatId) {
        return dogUserRepository.findDogUserByChatId(chatId);
    }

    public Optional<DogUser> getDogUser(Long id) {
        Optional<DogUser> findBotUser = dogUserRepository.findById(id);
        return findBotUser;
    }

    public void deleteDogUser(Long id) {
        dogUserRepository.deleteById(id);
    }

    public DogUser getDogUserByPhoneNumber(Long phoneNumber) {
        return dogUserRepository.findDogUserByPhoneNumber(phoneNumber);
    }

    public DogUser editDogUser(DogUser dogUser) {
        return dogUserRepository.save(dogUser);
    }

    public List<DogUser> getAllDogUser() {
        return List.copyOf(dogUserRepository.findAll());
    }

}
