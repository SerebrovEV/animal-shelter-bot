package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.CatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatUserRepository extends JpaRepository<CatUser, Long> {

    Optional<CatUser> findCatUserByChatId(Long chatId);

    Optional<CatUser> findByPhoneNumber(Long phoneNumber);
}
