package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    public List<Complaint> findComplaintsByIsOpenTrueAndComplaintOpenAfter(Date date);

    public List<Complaint> findComplaintsByStore(Store store);
}
