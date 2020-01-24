package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.PrivilegeRepository;
import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.Privilege;
import org.cshaifasweng.winter.models.Report;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.cshaifasweng.winter.services.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;
    private final PrivilegeRepository privilegeRepository;

    public ReportController(ReportService reportService, UserRepository userRepository, PrivilegeRepository privilegeRepository) {
        this.reportService = reportService;
        this.userRepository = userRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @GetMapping("/report")
    public List<Report> listAllReports() {
        return reportService.findAll();
    }

    @GetMapping("/report/{id}")
    public List<Report> listReportsByStore(@PathVariable("id") long id, Authentication authentication) throws LogicalException {
        Employee employee = (Employee) userRepository.findByEmail(authentication.getName());
        Privilege privilegeAll = privilegeRepository.findByName(SecurityConstants.PRIVILEGE_REPORTS_VIEW_ALL);
        if (!authentication.getAuthorities().contains(privilegeAll)
        && employee.getManagedStore().getId() != id)
            throw new LogicalException("You are not allowed to view this store's logs");

        return reportService.findByStore(id);
    }
}
