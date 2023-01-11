package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.DogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogUserRepository extends JpaRepository<DogUser, Long> {
    DogUser findBotUserByChatId(Long id);

    DogUser findByPhoneNumber(Long phoneNumber);
}
