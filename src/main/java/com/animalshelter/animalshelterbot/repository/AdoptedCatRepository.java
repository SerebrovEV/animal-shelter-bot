package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AdoptedCatRepository extends JpaRepository<AdoptedCat, Long> {
    Collection<AdoptedCat> findAdoptedCatsByCatUser_Id(Long id);


    Optional<AdoptedCat> findAdoptedCatByCatName(String name);
}
