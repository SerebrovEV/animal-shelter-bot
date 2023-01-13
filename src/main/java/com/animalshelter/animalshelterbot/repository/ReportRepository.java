package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<DogReport, Long> {
}
