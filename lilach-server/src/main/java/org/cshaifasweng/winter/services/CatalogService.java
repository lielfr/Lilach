package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {
    private final CatalogItemsRepository repository;
    private final StoreRepository storeRepository;

    @Autowired
    public CatalogService(CatalogItemsRepository repository, StoreRepository storeRepository) {
        this.repository = repository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public List<CatalogItem> findAll() {
        return repository.findAll();
    }

    @Transactional
    public List<CatalogItem> findByStore(long id) {
        Store store = storeRepository.getOne(id);

        return repository.findByStore(store);
    }

    @Transactional
    public CatalogItem updateItem(long id, CatalogItem item) {
        // TODO: add validation
        CatalogItem dbItem = repository.getOne(id);
        dbItem.setDescription(item.getDescription());
        dbItem.setAvailableCount(item.getAvailableCount());
        dbItem.setDominantColor(item.getDominantColor());
        dbItem.setItemsSold(item.getItemsSold());
        dbItem.setPicture(item.getPicture());
        dbItem.setPrice(item.getPrice());
        repository.save(dbItem);
        return dbItem;
    }

    @Transactional
    public CatalogItem addItem(CatalogItem item) {
        // TODO: Add validation
        repository.save(item);
        return item;
    }

}
