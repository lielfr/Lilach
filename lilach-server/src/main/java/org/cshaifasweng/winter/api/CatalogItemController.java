package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.CatalogItemType;
import org.cshaifasweng.winter.services.CatalogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public List<CatalogItem> getItemsByStore(@PathVariable("id") long id,
                                             @RequestParam(required = false, name = "singleItems")
                                                     Optional<Boolean> singleItems) {
        if (singleItems.isPresent() && singleItems.get())
            return catalogService.findByStoreAndType(id, CatalogItemType.ONE_FLOWER);
        return catalogService.findByStore(id);
    }

    @PutMapping("/catalog/{id}")
    public CatalogItem updateItem(@PathVariable("id") long id, @RequestBody CatalogItem item) {
        return catalogService.updateItem(id, item);
    }

    @PostMapping("/store/{id}/catalog")
    public CatalogItem newItem(@PathVariable("id") long id, @RequestBody CatalogItem item) {
        return catalogService.addItem(id, item);
    }
}
