package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.SpringServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@RestController
public class ImageController {
    @GetMapping("/image/{id}")
    public byte[] getImage(@PathVariable("id") String id) throws IOException {
        System.out.println("ID: " + id);
        File file = new File(String.valueOf(SpringServer.class.getResource("uploads/" + id)));
        return new FileInputStream(file).readAllBytes();
    }

    @PostMapping("/image")
    public String uploadImage(@RequestBody MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID() + "." + file.getOriginalFilename().split(".")[1];
        FileOutputStream outputStream =
                new FileOutputStream(
                        new File(
                                String.valueOf(SpringServer.class.getResource("uploads")) + "/" + uuid));
        outputStream.write(file.getBytes());
        outputStream.close();
        return uuid;
    }
}
