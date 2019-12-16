package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogItemsRepository extends JpaRepository<CatalogItem, Long> {
}
