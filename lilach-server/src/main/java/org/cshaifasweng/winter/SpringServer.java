package org.cshaifasweng.winter;


import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.models.CatalogItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class SpringServer {

    private static ConfigurableApplicationContext context = null;
    static SpringApplicationBuilder builder;
    private static SpringApplication app;
    static Properties properties;

    @Autowired
    private CatalogItemsRepository catalogItemsRepository;

    public SpringServer() {
        context = builder.context();
    }

    public static void main(String[] args) {
        SpringServer.builder = new SpringApplicationBuilder(SpringServer.class);
        properties = new Properties();
        App.main(args);
    }

    public static void runServer() throws IOException {

        try (InputStream inputStream = SpringServer.class.getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        SpringServer.app = builder.build();
        app.setDefaultProperties(properties);
        context = SpringServer.app.run();
    }

    public static void stopServer() {
        if (context != null)
            context.close();
    }

    private byte[] imageAsBytes(String name) throws IOException {
        return getClass().getResourceAsStream(name).readAllBytes();
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            // TODO: Initialize the data here (or maybe somewhere else?)
            List<CatalogItem> items = new ArrayList<>();
            items.add(new CatalogItem(25, "Just another flower",
                    "Blue",
                    imageAsBytes("flower1.jpg"),
                    4));
            items.add(new CatalogItem(15, "A cheaper flower",
                    "White",
                    imageAsBytes("flower2.jpg"),
                    3));
            items.add(new CatalogItem(30, "Classic Rose", "Red",
                    imageAsBytes("flower3.jpg"),
                    1));
            items.add(new CatalogItem(10, "Cheapest flower available", "White",
                    imageAsBytes("flower4.jpg"),
                    5));
            items.add(new CatalogItem(40, "A flower in the sun (pun intended)", "Yellow",
                    imageAsBytes("flower5.jpg"),
                    0));
            catalogItemsRepository.saveAll(items);
        };
    }
}
