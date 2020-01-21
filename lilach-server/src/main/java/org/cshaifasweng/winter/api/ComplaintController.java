package org.cshaifasweng.winter.api;

import org.apache.juli.logging.Log;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.services.ComplaintService;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ComplaintController {
    private final ComplaintService complaintService;
    private final UserService userService;

    public ComplaintController(ComplaintService complaintService, UserService userService) {
        this.complaintService = complaintService;
        this.userService = userService;
    }

    @PostMapping("/complaint")
    public Complaint newComplaint(@RequestBody Complaint complaint,
                                  Authentication authentication) throws LogicalException {
        if (authentication == null) throw new LogicalException("Authentication cannot be null");
        User user = userService.getUser(authentication.getName());
        if (!(user instanceof Customer)) throw new LogicalException("User should be a customer");
        return complaintService.newComplaint(complaint.getDescription(), complaint.isEmail(), (Customer) user);
    }

    @GetMapping("/complaint/{id}")
    public Complaint getComplaint(@PathVariable("id") Long id) throws LogicalException {
        if (id == null) throw new LogicalException("Id required");

        return complaintService.getComplaint(id);
    }

    @PutMapping("/complaint/{id}")
    public Complaint handleComplaint(@PathVariable("id") Long id, @RequestBody Complaint complaint,
                                     Authentication authentication) throws LogicalException {
        if (id == null) throw new LogicalException("Id required");
        if (authentication == null) throw new LogicalException("Authentication cannot be null");
        if (complaint == null) throw new LogicalException("Complaint must not be empty");

        User user = userService.getUser(authentication.getName());
        if (!(user instanceof Employee)) throw new LogicalException("User should be an employee");

        Map<String, Object> requiredFields = new HashMap<>();
        requiredFields.put("answer", complaint.getAnswer());
        requiredFields.put("compensation", complaint.getCompensation());

        List<String> missingFields = new ArrayList<>();

        for (Map.Entry<String, Object> entry : requiredFields.entrySet()) {
            if (entry.getValue() == null)
                missingFields.add(entry.getKey());
        }

        if (!missingFields.isEmpty())
            throw new LogicalException("Missing field(s): " + String.join(", ", missingFields));


        return complaintService.handleComplaint(id, complaint.getAnswer(),
                complaint.getCompensation(), (Employee)user);
    }

}
