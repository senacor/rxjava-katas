package com.senacor.codecamp.reactive.katas.introduction.solution;

import com.senacor.codecamp.reactive.concurrency.model.Article;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * @author Dr. Michael Menzel
 */
public class Kata2FetchArticleFlux {

    private final WikiService wikiService = WikiService.create("en");

    @Test
    public void createAnFlux() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an Flux that emits the plane type
        // 2) use the fetch article method to transform the plane type to an Article
        // 3) subscribe to the observable and print the article content

        Flux.fromArray(planeTypes)
                .flatMap(planeType -> fetchArticle(planeType))
                .subscribe(article -> ReactiveUtil.print("next: %s", article.content),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(10, TimeUnit.SECONDS);
    }


    /**
     * fetches an article from the wikipedia
     *
     * @param articleName name of the wikipedia article
     * @return an article
     */
    private Flux<Article> fetchArticle(String articleName) {
        return wikiService.fetchArticleFlux(articleName)
                .map((article) -> new Article(articleName, article));
    }
}
