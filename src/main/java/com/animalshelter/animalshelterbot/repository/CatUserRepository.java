package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.CatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatUserRepository extends JpaRepository<CatUser, Long> {
    CatUser findCatUserByChatId(Long chatId);

    CatUser findByPhoneNumber(Long phoneNumber);
}
