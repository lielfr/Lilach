package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonTypeName("complaints_report")
public class ComplaintsReport extends Report {
    @ElementCollection
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
