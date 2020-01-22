package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByName(String name);
}
