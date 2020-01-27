package org.cshaifasweng.winter;


import org.cshaifasweng.winter.da.CatalogItemsRepository;
import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SpringServer {

    private static ConfigurableApplicationContext context = null;
    private static SpringApplicationBuilder builder;
    private static SpringApplication app;
    private static Properties properties;

    private final CatalogItemsRepository catalogItemsRepository;

    private final MailService mailService;

    private final UserRepository userRepository;

    private final CustomerRepository customerRepository;

    private static final Logger log = LoggerFactory.getLogger(SpringServer.class);

    public SpringServer(CatalogItemsRepository catalogItemsRepository, MailService mailService, UserRepository userRepository, CustomerRepository customerRepository) {
        context = builder.context();
        this.catalogItemsRepository = catalogItemsRepository;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) throws IOException {
        builder = new SpringApplicationBuilder(SpringServer.class);
        properties = new Properties();
        try (InputStream inputStream = SpringServer.class.getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        app = builder.build();
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
            log.info("Server is up and running!");
        };

    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
