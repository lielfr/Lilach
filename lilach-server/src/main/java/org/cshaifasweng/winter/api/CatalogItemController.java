package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.services.CatalogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CatalogItemController {

    private final CatalogItemsRepository repository;
    private final StoreRepository storeRepository;
    private final CatalogService catalogService;

    public CatalogItemController(CatalogItemsRepository repository, StoreRepository storeRepository, CatalogService catalogService) {
        this.repository = repository;
        this.storeRepository = storeRepository;
        this.catalogService = catalogService;
    }

    @GetMapping("/catalog")
    public List<CatalogItem> getItems() {
        return catalogService.findAll();
    }

    @GetMapping("/store/{id}/catalog")
    public List<CatalogItem> getItemsByStore(@PathVariable("id") long id) {
        return catalogService.findByStore(id);
    }

    @PutMapping("/catalog/{id}")
    public CatalogItem updateItem(@PathVariable("id") long id, @RequestBody CatalogItem item) {
        return catalogService.updateItem(id, item);
    }

    @PostMapping("/catalog")
    public CatalogItem newItem(@RequestBody CatalogItem item) {
        return catalogService.addItem(item);
    }
}
