package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.SpringServer;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class ImageController {
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) throws IOException, URISyntaxException {
        System.out.println("ID: " + id);
        File file = new File(SpringServer.class.getResource("uploads/" + id).toURI());
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String mimeType = fileTypeMap.getContentType(file);
        byte[] fileAsBytes = new FileInputStream(file).readAllBytes();
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok()
                .header("Content-Type", mimeType)
                .body(fileAsBytes);
        return responseEntity;
    }

    @PostMapping("/image")
    public String uploadImage(@RequestBody MultipartFile file) throws IOException, LogicalException {
        String uuid = UUID.randomUUID().toString();
        File newFile = new File(
                SpringServer.class.getResource("uploads") + "/" + uuid);
        if (!newFile.createNewFile())
            throw new LogicalException("File exists");
        FileOutputStream outputStream =
                new FileOutputStream(newFile);
        outputStream.write(file.getBytes());
        outputStream.close();
        return uuid;
    }
}
