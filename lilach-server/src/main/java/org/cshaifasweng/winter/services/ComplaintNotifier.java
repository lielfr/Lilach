package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.ComplaintRepository;
import org.cshaifasweng.winter.da.EmployeeRepository;
import org.cshaifasweng.winter.da.RoleRepository;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class ComplaintNotifier {

    private final ComplaintRepository complaintRepository;
    private final MailService mailService;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public ComplaintNotifier(ComplaintRepository complaintRepository, MailService mailService, EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.complaintRepository = complaintRepository;
        this.mailService = mailService;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }


    @Scheduled(fixedRateString = "${lilach.scheduling.complaint-notifying}")
    public void notifyUnhandledComplaints() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        List<Complaint> unhandledComplaints =
                complaintRepository.findComplaintsByIsOpenTrueAndComplaintOpenAfter(calendar.getTime());

        for (Complaint complaint : unhandledComplaints) {

            if (complaint.isEmail()) {
                Customer customer = complaint.getOpenedBy();
                String email = complaint.getOpenedBy().getEmail();
                mailService.sendMail(email, "Complaint Status",
                        "<h2>Dear " + customer.getFirstName() + " " + customer.getLastName() + "</h2>,<br>" +
                                "<p>We apologize for the inconvenience, but it seems that your complaint is taking" +
                                "a bit of time to handle." +
                                "Please note that the Lilach store chain" +
                                "respects your time and puts its customers in the first place.</p><br>" +
                                "<p>Sincerely,<br>" +
                                "Lilach Team.</p>");
            }

            Store store = complaint.getStore();

            List<Employee> toNotify = new ArrayList<>();
            toNotify.addAll(employeeRepository.findAllByRolesContaining(
                    roleRepository.findByName(SecurityConstants.ROLE_ADMIN)));
            toNotify.addAll(
                    employeeRepository.findAllByRolesContaining(
                            roleRepository.findByName(SecurityConstants.ROLE_STORE_CHAIN_MANAGER)));

            if (store != null) {
                toNotify.add(store.getManager());
            }

            toNotify.forEach((employee -> {
                mailService.sendMail(employee.getEmail(), "URGENT: Unhandled complaint!",
                                "<p>Please handle complaint no. " + complaint.getId() + " urgently.</p>");
            }));
        }
    }
}
