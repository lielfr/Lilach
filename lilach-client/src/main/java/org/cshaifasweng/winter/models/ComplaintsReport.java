package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("complaints_report")
public class ComplaintsReport extends Report {
    private List<Long> complaintsByMonth;

    public ComplaintsReport() {
        this.complaintsByMonth = new ArrayList<>();
    }

    public List<Long> getComplaintsByMonth() {
        return complaintsByMonth;
    }

    public void setComplaintsByMonth(List<Long> complaintsByMonth) {
        this.complaintsByMonth = complaintsByMonth;
    }
}
