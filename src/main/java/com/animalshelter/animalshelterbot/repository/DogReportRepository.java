package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport, Long> {
}
