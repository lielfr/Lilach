package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.CustomItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomItemsRepository extends JpaRepository<CustomItem, Long> {
}
