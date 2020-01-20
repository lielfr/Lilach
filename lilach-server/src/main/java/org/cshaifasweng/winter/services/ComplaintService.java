package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.ComplaintRepository;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Transactional
    public Complaint newComplaint(String description, boolean email, Customer openedBy) {
        Complaint complaint = new Complaint(description, email, openedBy);
        complaint.setComplaintOpen(new Date());
        complaintRepository.save(complaint);

        return complaint;
    }

    @Transactional
    public Complaint getComplaint(Long id) {
        Complaint complaint = complaintRepository.getOne(id);

        return complaint;
    }
}
