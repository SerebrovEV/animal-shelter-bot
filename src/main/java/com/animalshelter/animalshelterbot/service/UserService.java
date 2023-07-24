package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.PetUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    PetUser addUser(PetUser user);

    <T> Optional<T> getUserByChatId(Long chatId);

    <T> Optional<T> getUserByPhoneNumber(Long phoneNumber);

    PetUser editUser(PetUser petUser);

    <T> Optional<T> getUser(Long id);

    void deleteUser(Long id);

    <T> List<T> getAllUser();
}
