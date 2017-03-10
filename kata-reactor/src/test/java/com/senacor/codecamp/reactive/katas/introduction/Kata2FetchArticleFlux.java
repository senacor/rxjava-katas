package com.senacor.codecamp.reactive.katas.introduction;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.Watch;
import com.senacor.codecamp.reactive.concurrency.model.Article;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Dr. Michael Menzel
 */
public class Kata2FetchArticleFlux {
    private final WikiService wikiService = WikiService.create("en");

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void createAnFlux() throws Exception {
        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an Flux that emits the plane type
        // 2) use the fetch article method to transform the plane type to an Article
        // 3) subscribe to the observable and print the article content

    }

    /**
     * fetches an article from the wikipedia
     *
     * @param articleName name of the wikipedia article
     * @return an article
     */
    private Article fetchArticle(String articleName) {
        return new Article(articleName, wikiService.fetchArticle(articleName));
    }

}
