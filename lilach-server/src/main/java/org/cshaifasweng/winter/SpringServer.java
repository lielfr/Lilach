package org.cshaifasweng.winter;


import org.cshaifasweng.winter.da.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class SpringServer {

    private static ConfigurableApplicationContext context = null;
    static SpringApplicationBuilder builder;
    private static SpringApplication app;
    static Properties properties;

    @Autowired
    private CustomerRepository customerRepository;

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

    @Bean
    CommandLineRunner runner() {
        return args -> {
            // TODO: Initialize the data here (or maybe somewhere else?)
        };
    }
}
