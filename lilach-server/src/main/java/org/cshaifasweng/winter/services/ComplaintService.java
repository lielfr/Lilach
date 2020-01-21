package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.ComplaintRepository;
import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, CustomerRepository customerRepository) {
        this.complaintRepository = complaintRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Complaint newComplaint(String description, boolean email, Customer openedBy) {
        Complaint complaint = new Complaint(description, email, openedBy);
        complaint.setComplaintOpen(new Date());
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

        complaint.setComplaintClose(new Date());
        complaint.setOpen(false);
        complaintRepository.save(complaint);

        return complaint;
    }
}
