package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.models.CatalogItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CatalogItemController {

    private final CatalogItemsRepository repository;

    public CatalogItemController(CatalogItemsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/catalog")
    public List<CatalogItem> getItems() {
        return repository.findAll();
    }

    @PutMapping("/catalog/{id}")
    public CatalogItem updateItem(@PathVariable("id") long id, @RequestBody CatalogItem item) {
        // TODO: add validation
        // TODO: Maybe move all of that stuff into a method in CatalogItem
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
}
