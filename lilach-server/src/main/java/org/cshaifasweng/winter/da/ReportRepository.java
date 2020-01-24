package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
