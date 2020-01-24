package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByStoreId(long id);
}
