package com.animalshelter.animalshelterbot.service.impl;


import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.model.PetUser;
import com.animalshelter.animalshelterbot.repository.CatUserRepository;
import com.animalshelter.animalshelterbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <i>Сервис для добавления, получения, редактирования и удаления контактных данных усыновителей
 * {@link CatUser} в/из базы данных приюта для кошек</i>
 */

@Service
@RequiredArgsConstructor
public class CatUserService implements UserService {
    private final CatUserRepository catUserRepository;

    @Override
    public CatUser addUser(PetUser catUser) {
        return catUserRepository.save((CatUser) catUser);
    }

    @Override
    public Optional<CatUser> getUserByChatId(Long chatId) {
        return catUserRepository.findCatUserByChatId(chatId);
    }

    @Override
    public Optional<CatUser> getUserByPhoneNumber(Long phoneNumber) {
        return catUserRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public CatUser editUser(PetUser catUser) {
        return catUserRepository.save((CatUser) catUser);
    }

    @Override
    public Optional<CatUser> getUser(Long id) {
        return catUserRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        catUserRepository.deleteById(id);
    }

    @Override
    public List<CatUser> getAllUser() {
        return List.copyOf(catUserRepository.findAll());
    }

}
