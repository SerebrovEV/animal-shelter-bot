package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface AdoptedCatRepository extends JpaRepository<AdoptedCat, Long> {

    Optional<AdoptedCat> findAdoptedCatByCatName(String name);

    @Query(value =
            "select ac from AdoptedCat ac " +
            "left join CatReport cr on ac.id = cr.adoptedCat.id and cr.date = :date " +
                    "where cr.id is null ")
    Collection<AdoptedCat> findMissingReportAdoptedCat(@Param("date") Date date);
}
