package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.CustomItemsRepository;
import org.cshaifasweng.winter.models.CustomItem;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomItemController {

    private final CustomItemsRepository customItemsRepository;

    public CustomItemController(CustomItemsRepository customItemsRepository) {
        this.customItemsRepository = customItemsRepository;
    }

    @PostMapping("/custom")
    public CustomItem newCustomItem(@RequestBody CustomItem item) {
        // TODO: Add validation
        return customItemsRepository.save(item);
    }

    @DeleteMapping("/custom/{id}")
    public void deleteCustomITem(@PathVariable("id") long id) {
        customItemsRepository.deleteById(id);
    }
}
