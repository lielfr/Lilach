package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}
