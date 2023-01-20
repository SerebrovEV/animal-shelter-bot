package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport, Long> {

    Collection<DogReport> findDogReportByDate(Date date);

    Collection<DogReport> findDogReportsByAdoptedDog_Id(Long id);

    Optional<DogReport> findDogReportByDateAndAdoptedDog_Id(Date date, Long id);
}
