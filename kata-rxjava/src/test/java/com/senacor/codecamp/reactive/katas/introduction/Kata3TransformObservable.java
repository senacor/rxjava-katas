package com.senacor.codecamp.reactive.katas.introduction;

import com.senacor.codecamp.reactive.concurrency.model.Article;
import com.senacor.codecamp.reactive.concurrency.model.PlaneInfo;
import com.senacor.codecamp.reactive.services.PlainInfoService;
import com.senacor.codecamp.reactive.services.WikiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

/**
 * @author Dr. Michael Menzel
 */
public class Kata3TransformObservable {
    private final WikiService wikiService = WikiService.create("en");
    private final PlainInfoService plainInfoService = new PlainInfoService();

    @Test
    public void createAnObservable() throws Exception {
        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an observable that emits the plane type
        // 2) use the fetchArticleObservable method to transform the plane type to an Article
        // 3) use the parsePlaneInfo method to transform the article to an planeInfo object
        // 4) subscribe to the observable and print the plane information

    }

    /**
     * fetches an article from the wikipedia
     *
     * @param articleName name of the wikipedia article
     * @return an article
     */
    Observable<Article> fetchArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .map((article) -> new Article(articleName, article));
    }

    /**
     * Extracts plane-related information from an wikipedia article
     *
     * @param article wikipedia article
     * @return plane information
     */
    PlaneInfo parsePlaneInfo(Article article) {
        return new PlaneInfo(article.name, plainInfoService.parseBuildCountInt(article.content));
    }

}
