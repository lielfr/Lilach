package org.cshaifasweng.winter.loaders;

import org.cshaifasweng.winter.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@EnableAutoConfiguration
@Configuration
public class SearchConfig {
    private final EntityManager entityManager;

    @Autowired
    public SearchConfig(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Bean
    SearchService searchService() throws InterruptedException {
        SearchService searchService = new SearchService(entityManager);
        searchService.initializeHibernateSearch();
        return searchService;
    }
}
