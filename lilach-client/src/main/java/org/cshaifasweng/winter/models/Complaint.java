package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Complaint {
    private Long id;

//    private boolean purchased;
    private String description;

    private boolean email;

    private String answer;

    private Double compensation = 0.0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date complaintOpen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date complaintClose;

    public Complaint() {
    }

    public Complaint(String description, boolean email) {
        this.description = description;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Double getCompensation() {
        return compensation;
    }

    public void setCompensation(Double compensation) {
        this.compensation = compensation;
    }

    public Date getComplaintOpen() {
        return complaintOpen;
    }

    public void setComplaintOpen(Date complaintOpen) {
        this.complaintOpen = complaintOpen;
    }

    public Date getComplaintClose() {
        return complaintClose;
    }

    public void setComplaintClose(Date complaintClose) {
        this.complaintClose = complaintClose;
    }
}
