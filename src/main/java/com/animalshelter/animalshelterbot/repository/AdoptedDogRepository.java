package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface AdoptedDogRepository extends JpaRepository<AdoptedDog, Long> {
    Optional<AdoptedDog> findAdoptedDogByDogUserId(Long dogUserid);
    Optional<AdoptedDog> findAdoptedDogByAdoptionDate(Date adoptionDate);
    List<AdoptedDog> findAllByDogUserIsNull();
   // List<AdoptedDog> findAllByTrialPeriodIsNotNull();
   List<AdoptedDog> findAllByDogUserIsNotNull();

    Optional<AdoptedDog> findAdoptedDogByDogName(String name);

    @Query(value =
            "select ad from AdoptedDog ad " +
                    "left join DogReport dr on ad.id = dr.adoptedDog.id and dr.date = :date " +
                    "where dr.id is null and ad.adoptionDate < :date")
    Collection<AdoptedDog> findMissingReportAdoptedDog(@Param("date") Date date);
}
