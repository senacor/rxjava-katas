package com.senacor.tecco.reactive.katas.introduction.solution;

import com.senacor.tecco.reactive.concurrency.model.Article;
import com.senacor.tecco.reactive.concurrency.model.PlaneInfo;
import com.senacor.tecco.reactive.services.PlainInfoService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.WaitMonitor;
import com.senacor.tecco.reactive.util.Watch;
import org.junit.Rule;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class Kata3TransformFlux {
    private final WikiService wikiService = WikiService.create("en");
    private final PlainInfoService plainInfoService = new PlainInfoService();

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void createAnFlux() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an Flux that emits the plane type
        // 2) use the fetchArticle method to transform the plane type to an Article
        // 3) use the parsePlaneInfo method to transform the article to an planeInfo object
        // 4) subscribe to the observable and print the plane information


        Flux.fromArray(planeTypes)
                .flatMap(planeType -> fetchArticle(planeType))
                .map(article -> parsePlaneInfo(article))
                .subscribe(planeInfo -> print("next: %s", planeInfo),
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
        return Flux.just(wikiService.fetchArticle(articleName))
                .subscribeOn(Schedulers.elastic())
                .map((article) -> new Article(articleName, article));
        /*
        return wikiService.fetchArticleFlux(articleName).
                map((article) -> new Article(articleName, article));
                */
    }

    /**
     * Extracts plane-related information from an wikipedia article
     *
     * @param article wikipedia article
     * @return plane information
     */
    private PlaneInfo parsePlaneInfo(Article article) {
        return new PlaneInfo(article.name, plainInfoService.parseBuildCountInt(article.content));
    }
}
