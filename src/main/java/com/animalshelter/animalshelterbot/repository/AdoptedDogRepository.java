package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptedDogRepository extends JpaRepository<AdoptedDog, Long> {
    Optional<AdoptedDog> findAdoptedDogByDogUserId(Long dogUserid);
    Optional<AdoptedDog> findAdoptedDogByAdoptionDate(Date adoptionDate);
    List<AdoptedDog> findAllByDogUserIsNull();
   // List<AdoptedDog> findAllByTrialPeriodIsNotNull();
   List<AdoptedDog> findAllByDogUserIsNotNull();

}
