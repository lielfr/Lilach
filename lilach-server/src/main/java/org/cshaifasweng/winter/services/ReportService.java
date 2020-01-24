package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.ReportRepository;
import org.cshaifasweng.winter.models.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public List<Report> findByStore(long id) {
        return reportRepository.findAllByStoreId(id);
    }
}
