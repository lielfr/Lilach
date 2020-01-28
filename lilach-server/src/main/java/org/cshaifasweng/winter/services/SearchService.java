package org.cshaifasweng.winter.services;

import org.apache.lucene.search.Query;
import org.cshaifasweng.winter.models.CatalogItem;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final EntityManager entityManager;

    @Autowired
    public SearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void initializeHibernateSearch() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }

    @Transactional
    public List<CatalogItem> searchItems(String keywords, long storeId) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(CatalogItem.class)
                .overridesForField("name", "ngram_query")
                .get();
        Query luceneQuery = queryBuilder.keyword()
                .onFields("description", "id").matching(keywords).createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, CatalogItem.class);

        List<CatalogItem> catalogItemList = null;
        try {
            catalogItemList = jpaQuery.getResultList();
            catalogItemList = catalogItemList.stream().filter((item) -> item.getStore().getId() == storeId)
                    .collect(Collectors.toList());
        } catch (NoResultException exception) {
            // Do nothing
        }

        return catalogItemList;
    }
}
