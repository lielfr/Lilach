package org.cshaifasweng.winter.api;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    @GetMapping("/image/{id}")
    public Resource getImage(@PathVariable String id) {
        return null;
    }
}
