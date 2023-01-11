package com.animalshelter.animalshelterbot.service;


import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.repository.CatUserRepository;
import com.animalshelter.animalshelterbot.repository.DogUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <i>Сервис для добавления, получения, редактирования и удаления контактных данных усыновителей
 * {@link CatUser} в/из базы данных</i>
 */

@Service
@RequiredArgsConstructor
public class CatUserService {
    private final CatUserRepository catUserRepository;

    public CatUser addCatUser(CatUser catUser) {
        return catUserRepository.save(catUser);
    }

    public CatUser getCatUserByChatId(Long chatId) {
        return catUserRepository.findCatUserByChatId(chatId);
    }

    public CatUser getByPhoneNumber(Long phoneNumber) {
        return catUserRepository.findByPhoneNumber(phoneNumber);
    }

    public CatUser editCatUser(CatUser catUser) {
        return catUserRepository.save(catUser);
    }

    public void deleteCatUser(Long id) {
        catUserRepository.deleteById(id);
    }

    public List<CatUser> getAllCatUser() {
        return List.copyOf(catUserRepository.findAll());
    }

}
