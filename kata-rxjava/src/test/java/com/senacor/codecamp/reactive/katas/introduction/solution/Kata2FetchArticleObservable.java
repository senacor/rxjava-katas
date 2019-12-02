package com.senacor.codecamp.reactive.katas.introduction.solution;

import com.senacor.codecamp.reactive.concurrency.model.Article;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class Kata2FetchArticleObservable {

    private final WikiService wikiService = WikiService.create("en");

    @Test
    public void createAnObservable() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an observable that emits the plane type
        // 2) use the fetch article method to transform the plane type to an Article
        // 3) subscribe to the observable and print the article content

        Observable.fromArray(planeTypes)
                .flatMap(planeType -> fetchArticle(planeType))
                .subscribe(article -> print("next: %s", article.content),
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
    Observable<Article> fetchArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .map((article) -> new Article(articleName, article));
    }
}
