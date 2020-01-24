package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.models.Store;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
}
