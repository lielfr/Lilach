package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogItemsRepository extends JpaRepository<CatalogItem, Long> {
    CatalogItem findByStoreAndDescription(Store store, String description);

    List<CatalogItem> findByStore(Store store);

    List<CatalogItem> findByStoreOrderByDiscountAmountAscDiscountPercentAsc(Store store);
}
