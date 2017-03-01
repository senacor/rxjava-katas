package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private final WikiService wikiService = new WikiService();
    private final RatingService ratingService = RatingServiceImpl.create();
    private final CountService countService = CountServiceImpl.create();

    /**
     * 1. fetch and parse a wiki article
     * 2. use {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}.
     * Combine both information as JSON and print the JSON to the console.
     * Example {"articleName": "Superman", "rating": 3, "wordCount": 452}
     */
    @Test
    public void combiningObservable() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        wikiService.fetchArticleFlux(wikiArticle)
                   .flatMap(wikiService::parseMediaWikiTextFlux)
                   .flatMap(parsedPage -> {
                       Flux<ParsedPage> p = Flux.just(parsedPage);
                       Flux<Integer> rating = p.flatMap(ratingService::rateFlux);
                       Flux<Integer> wordCount = p.flatMap(countService::countWordsFlux);

                       return rating.zipWith(wordCount,
                               (r, wc) -> String.format("{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                                       wikiArticle, r, wc)
                       );
                   })
                   .subscribe(next -> print("next: %s", next),
                           Throwable::printStackTrace,
                           waitMonitor::complete);

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

    @Test
    public void combiningObservablePublish() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        ConnectableFlux<ParsedPage> connectableFlux = wikiService.fetchArticleFlux(wikiArticle)
                                                                 .flatMap(wikiService::parseMediaWikiTextFlux)
                                                                 .publish();

        Flux<Integer> ratingObservable = connectableFlux.flatMap(ratingService::rateFlux);
        Flux<Integer> wordCountObservable = connectableFlux.flatMap(countService::countWordsFlux);
        connectableFlux.connect();

        ratingObservable.zipWith(wordCountObservable, (r, wc) -> String.format(
                "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                wikiArticle, r, wc))
                        .subscribe(next -> print("next: %s", next),
                                Throwable::printStackTrace,
                                () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

    @Test
    public void combiningObservablePublish2() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        Flux<ParsedPage> pages = wikiService.fetchArticleFlux(wikiArticle)
                                            .flatMap(wikiService::parseMediaWikiTextFlux);

        Flux<Integer> ratingObservable = pages.flatMap(ratingService::rateFlux);
        Flux<Integer> wordCountObservable = pages.flatMap(countService::countWordsFlux);

        ratingObservable.zipWith(wordCountObservable, (r, wc) -> String.format(
                "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                wikiArticle, r, wc))
                        .subscribe(next -> print("next: %s", next),
                                Throwable::printStackTrace,
                                () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }
}