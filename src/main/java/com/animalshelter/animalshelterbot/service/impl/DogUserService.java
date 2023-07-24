package com.animalshelter.animalshelterbot.service.impl;


import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.model.PetUser;
import com.animalshelter.animalshelterbot.repository.DogUserRepository;
import com.animalshelter.animalshelterbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <i>Сервис для добавления, получения, редактирования и удаления контактных данных усыновителей
 * {@link DogUser} в/из базы данных приюта для собак</i>
 */

@Service
@RequiredArgsConstructor
public class DogUserService implements UserService {
    private final DogUserRepository dogUserRepository;

    @Override
    public DogUser addUser(PetUser dogUser) {
        return dogUserRepository.save((DogUser) dogUser);
    }

    @Override
    public Optional<DogUser> getUserByChatId(Long chatId) {
        return dogUserRepository.findDogUserByChatId(chatId);
    }

    @Override
    public Optional<DogUser> getUserByPhoneNumber(Long phoneNumber) {
        return dogUserRepository.findDogUserByPhoneNumber(phoneNumber);
    }

    public DogUser editUser(PetUser dogUser) {
        return dogUserRepository.save((DogUser) dogUser);
    }

    @Override
    public Optional<DogUser> getUser(Long id) {
        return dogUserRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        dogUserRepository.deleteById(id);
    }

    @Override
    public List<DogUser> getAllUser() {
        return List.copyOf(dogUserRepository.findAll());
    }

}
