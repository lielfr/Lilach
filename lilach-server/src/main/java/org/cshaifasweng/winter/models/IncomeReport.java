package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonTypeName("income_report")
public class IncomeReport extends Report {

    @ElementCollection
    private List<Double> incomeByMonth;

    private Double totalIncome;

    public IncomeReport() {
        this.incomeByMonth = new ArrayList<>();
        this.totalIncome = 0.0;
    }

    public List<Double> getIncomeByMonth() {
        return incomeByMonth;
    }

    public void setIncomeByMonth(List<Double> incomeByMonth) {
        this.incomeByMonth = incomeByMonth;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
