package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.CatalogItemType;
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

        return repository.findByStoreOrderByDiscountAmountDescDiscountPercentDesc(store);
    }

    @Transactional
    public CatalogItem updateItem(long id, CatalogItem item) {
        // TODO: add validation
        CatalogItem dbItem = repository.getOne(id);
        dbItem.setDescription(item.getDescription());
        dbItem.setAvailableCount(item.getAvailableCount());
        dbItem.setItemsSold(item.getItemsSold());
        dbItem.setPicture(item.getPicture());
        dbItem.setPrice(item.getPrice());
        repository.save(dbItem);
        return dbItem;
    }

    @Transactional
    public CatalogItem addItem(long storeId, CatalogItem item) {
        // TODO: Add validation
        item.setStore(storeRepository.getOne(storeId));
        repository.save(item);
        return item;
    }

    public List<CatalogItem> findByStoreAndType(long storeId, CatalogItemType type) {
        return repository.findByStoreAndItemType(storeRepository.getOne(storeId),
                type);
    }

    @Transactional
    public void deleteItem(long itemId) {
        repository.deleteById(itemId);
    }
}
