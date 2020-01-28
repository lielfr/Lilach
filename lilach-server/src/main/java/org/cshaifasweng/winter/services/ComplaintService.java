package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.ComplaintRepository;
import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, CustomerRepository customerRepository, UserRepository userRepository, MailService mailService) {
        this.complaintRepository = complaintRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Transactional
    public Complaint newComplaint(String description, boolean email, Customer openedBy, Store store) {
        Complaint complaint = new Complaint(description, email, openedBy);
        complaint.setComplaintOpen(new Date());
        complaint.setOpen(true);
        complaint.setStore(store);
        complaintRepository.save(complaint);

        return complaint;
    }

    @Transactional
    public Complaint getComplaint(long id) {
        Complaint complaint = complaintRepository.getOne(id);

        return complaint;
    }

    @Transactional
    public Complaint handleComplaint(long id, String answer, double compensation, Employee handledBy) {
        Complaint complaint = complaintRepository.getOne(id);
        complaint.setAnswer(answer);
        complaint.setHandledBy(handledBy);
        complaint.setCompensation(compensation);

        if (compensation > 0) {
            Customer customer = complaint.getOpenedBy();
            customer.getTransactions().add(new Transaction(new Date(),
                    "Compensation from complaint #" + complaint.getId(),
                    compensation
            ));

            customerRepository.save(customer);
        }

        if (complaint.isEmail()) {
            mailService.sendMail(complaint.getOpenedBy().getEmail(), "We got you!",
                    "Dear customer,<br>We care a lot about our customers.<b>You contacted us about the following topic:<br>" +
                            complaint.getDescription() + "<br>Our answer:" + complaint.getAnswer() + "<br>" +
                            "Compensation: " + complaint.getCompensation() + ".<br>Sincerely,<br>The Lilach team.");
        }

        complaint.setComplaintClose(new Date());
        complaint.setOpen(false);
        complaintRepository.save(complaint);

        return complaint;
    }

    @Transactional
    public List<Complaint> getComplaints() {
        return complaintRepository.findAll();
    }

    @Transactional
    public List<Complaint> getComplaintsByCustomer(long id, Authentication authentication) throws LogicalException {
        User user = userRepository.findByEmail(authentication.getName());
        if (user.getId() != id &&
                !authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                        .contains(SecurityConstants.PRIVILEGE_COMPLAINTS_VIEW_ALL))
            throw new LogicalException("Unauthorized to view complaints list");
        Customer customer = customerRepository.getOne(id);
        return complaintRepository.findComplaintsByOpenedBy(customer);
    }
}
