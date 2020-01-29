package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.CatalogItemType;
import org.cshaifasweng.winter.services.CatalogService;
import org.cshaifasweng.winter.services.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CatalogItemController {

    private final CatalogItemsRepository repository;
    private final StoreRepository storeRepository;
    private final CatalogService catalogService;
    private final SearchService searchService;

    public CatalogItemController(CatalogItemsRepository repository, StoreRepository storeRepository, CatalogService catalogService, SearchService searchService) {
        this.repository = repository;
        this.storeRepository = storeRepository;
        this.catalogService = catalogService;
        this.searchService = searchService;
    }

    @GetMapping("/catalog")
    public List<CatalogItem> getItems() {
        return catalogService.findAll();
    }

    @GetMapping("/store/{id}/catalog")
    public List<CatalogItem> getItemsByStore(@PathVariable("id") long id,
                                             @RequestParam(required = false, name = "singleItems")
                                                     Optional<Boolean> singleItems,
                                             @RequestParam(required = false, name = "search")
                                             Optional<String> query) {
        if (singleItems.isPresent() && singleItems.get())
            return catalogService.findByStoreAndType(id, CatalogItemType.ONE_FLOWER);
        if (query.isPresent()) {
            return searchService.searchItems(query.get(), id);
        }
        return catalogService.findByStore(id);
    }

    @PutMapping("/store/{sid}/catalog/{id}")
    public CatalogItem updateItem(@PathVariable("id") long id, @PathVariable("sid") long storeId, @RequestBody CatalogItem item) {
        return catalogService.updateItem(id, storeId, item);
    }

    @PostMapping("/store/{id}/catalog")
    public CatalogItem newItem(@PathVariable("id") long id, @RequestBody CatalogItem item) {
        return catalogService.addItem(id, item);
    }

    @DeleteMapping("/catalog/{id}")
    public void deleteItem(@PathVariable("id") long id) {
        catalogService.deleteItem(id);
    }
}
