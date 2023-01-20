package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.DogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DogUserRepository extends JpaRepository<DogUser, Long> {
    Optional<DogUser> findDogUserByChatId(Long id);

    Optional<DogUser> findDogUserByPhoneNumber(Long phoneNumber);
}
