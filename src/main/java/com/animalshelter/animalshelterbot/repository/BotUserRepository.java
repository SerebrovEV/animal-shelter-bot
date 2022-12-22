package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    BotUser findBotUserByUserId(Long id);
}