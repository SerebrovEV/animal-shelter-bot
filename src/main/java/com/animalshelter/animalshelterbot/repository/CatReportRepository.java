package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.CatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface CatReportRepository extends JpaRepository<CatReport, Long> {
    Collection<CatReport> findCatReportByDate(Date date);

    Optional<CatReport> findCatReportByDateAndAdoptedCat_Id(Date date, Long id);
}
