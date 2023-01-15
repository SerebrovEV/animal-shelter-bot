package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.CatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatReportRepository extends JpaRepository<CatReport, Long> {
}
