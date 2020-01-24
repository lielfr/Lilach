package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.services.StoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/store")
    public List<Store> allStores() {
        return storeService.getAllStores();
    }
}
