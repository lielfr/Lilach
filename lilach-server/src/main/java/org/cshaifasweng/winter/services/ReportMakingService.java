package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.*;
import org.cshaifasweng.winter.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReportMakingService {
    // TODO: Move this to the configuration file
    private static final long runningRate = 1000 * 3600;

    private static final Logger log = LoggerFactory.getLogger(ReportMakingService.class);

    private final ReportRepository reportRepository;
    private final StoreRepository storeRepository;
    private final ComplaintRepository complaintRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ReportMakingService(ReportRepository reportRepository, StoreRepository storeRepository, ComplaintRepository complaintRepository, CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.reportRepository = reportRepository;
        this.storeRepository = storeRepository;
        this.complaintRepository = complaintRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    private void genComplaintsReports() {
        log.info("Generating complaints reports");
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
            log.info("Generating income reports");
            IncomeReport report = new IncomeReport();
            List<Customer> customers = customerRepository.findAllByStoresContains(store);

            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.MONTH, -3);

            report.setStore(store);
            report.setStartDate(startDate.getTime());
            report.setEndDate(new Date());

            List<Double> incomesByMonths = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                double incomeByMonth = 0.0;
                for (Customer customer : customers) {
                    int finalI = i;
                    Optional<Double> sum = customer.getTransactions().stream()
                            .filter(transaction -> {
                                Calendar startOfMonth = Calendar.getInstance();
                                startOfMonth.set(Calendar.MONTH, finalI);
                                startOfMonth.set(Calendar.DAY_OF_MONTH, 1);

                                Calendar endOfMonth = (Calendar) startOfMonth.clone();
                                endOfMonth.set(Calendar.MONTH, endOfMonth.get(Calendar.MONTH) + 1);
                                return transaction.getDate().after(startOfMonth.getTime())
                                        && transaction.getDate().before(endOfMonth.getTime());
                            })
                            .map(Transaction::getAmount)
                            .reduce(Double::sum);
                    if (sum.isPresent())
                        incomeByMonth += sum.get();
                }
                incomesByMonths.add(incomeByMonth);
            }

            report.setIncomeByMonth(incomesByMonths);
            report.setTotalIncome(incomesByMonths.stream().reduce(Double::sum).orElse(0.0));

            reportRepository.save(report);
        }
    }

    private void genOrdersReport() {
        for (Store store : storeRepository.findAll()) {
            OrdersReport report = new OrdersReport();
            report.setStore(store);
            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.MONTH, -1);

            report.setStore(store);
            report.setStartDate(startDate.getTime());
            report.setEndDate(new Date());

            Map<CatalogItemType, Long> itemsByType = new HashMap<>();

            for (CatalogItemType type : CatalogItemType.values()) {
                itemsByType.put(
                        type,
                        orderRepository.findAllByStore(store).stream().
                                filter((order) ->
                                        order.getItems()
                                                .stream()
                                                .filter(item -> item instanceof CatalogItem)
                                                .map(item -> (CatalogItem) item)
                                                .filter(item -> item.getItemType() == type)
                                                .count() > 1
                                )
                                .count()
                );
            }

            report.setOrdersByType(itemsByType);
            report.setOrdersAmount(itemsByType.values().stream().reduce(Long::sum).orElse(0L));

            reportRepository.save(report);

        }
    }

    @Scheduled(fixedRate = runningRate)
    public void genReports() {
        log.info("Started generating reports");

        genComplaintsReports();
        genIncomeReports();
        genOrdersReport();
    }
}
