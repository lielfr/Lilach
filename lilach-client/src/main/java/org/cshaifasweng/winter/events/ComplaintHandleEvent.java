package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Complaint;

public class ComplaintHandleEvent {
    private Complaint complaint;

    public ComplaintHandleEvent(Complaint complaint) {
        this.complaint = complaint;
    }

    public Complaint getComplaint() {
        return complaint;
    }
}
