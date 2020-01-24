package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.ComplaintRepository;
import org.cshaifasweng.winter.da.ReportRepository;
import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.ComplaintsReport;
import org.cshaifasweng.winter.models.IncomeReport;
import org.cshaifasweng.winter.models.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Component
public class ReportMakingService {
    // TODO: Move this to the configuration file
    private static final long runningRate = 1000 * 3600;

    private static final Logger log = LoggerFactory.getLogger(ReportMakingService.class);

    private final ReportRepository reportRepository;
    private final StoreRepository storeRepository;
    private final ComplaintRepository complaintRepository;

    @Autowired
    public ReportMakingService(ReportRepository reportRepository, StoreRepository storeRepository, ComplaintRepository complaintRepository) {
        this.reportRepository = reportRepository;
        this.storeRepository = storeRepository;
        this.complaintRepository = complaintRepository;
    }

    private void genComplaintsReports() {
        for (Store store : storeRepository.findAll()) {
            ComplaintsReport report = new ComplaintsReport();
            List<Complaint> complaints = complaintRepository.findComplaintsByStore(store);

            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.MONTH, -3);

            report.setStore(store);
            report.setStartDate(startDate.getTime());
            report.setEndDate(new Date());

            List<Long> complaintsByMonth = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                int finalI = i;
                complaintsByMonth.add(complaints.stream().filter(complaint -> {
                    Calendar complaintOpen = new Calendar.Builder()
                            .setInstant(complaint.getComplaintOpen())
                            .build();
                    return complaintOpen.get(Calendar.MONTH) == finalI;
                }).count());
            }

            report.setComplaintsByMonth(complaintsByMonth);

            reportRepository.save(report);
        }
    }

    private void genIncomeReports() {
        for (Store store : storeRepository.findAll()) {
            IncomeReport report = new IncomeReport();

            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.MONTH, -3);

            report.setStore(store);
            report.setStartDate(startDate.getTime());
            report.setEndDate(new Date());
        }
    }

    @Scheduled(fixedRate = runningRate)
    public void genReports() {
        log.info("Started generating reports");

        genComplaintsReports();
    }
}
