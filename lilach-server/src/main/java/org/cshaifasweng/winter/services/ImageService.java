package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.SpringServer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class ImageService {
    public byte[] getImage(String id) throws IOException, URISyntaxException {
        File file = new File(SpringServer.class.getResource("uploads/" + id).toURI());
        return new FileInputStream(file).readAllBytes();
    }
}
