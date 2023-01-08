package com.animalshelter.animalshelterbot.repository;

import com.animalshelter.animalshelterbot.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
