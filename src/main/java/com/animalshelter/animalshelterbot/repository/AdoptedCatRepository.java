package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptedCatRepository extends JpaRepository<AdoptedCat, Long> {
    List<AdoptedCat> findAllByCatUserNotNull();

    List<AdoptedCat> findAllByCatUserIsNull();

}
