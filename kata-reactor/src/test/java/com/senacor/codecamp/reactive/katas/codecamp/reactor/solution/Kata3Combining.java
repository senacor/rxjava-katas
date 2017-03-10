package com.senacor.codecamp.reactive.katas.codecamp.reactor.solution;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata3Combining {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    /**
     * 1. fetch and parse a wiki article
     * 2. use {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}.
     * Combine both information as JSON and print the JSON to the console.
     * Example {"articleName": "Superman", "rating": 3, "wordCount": 452}
     */
    @Test
    public void combining() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        wikiService.fetchArticleFlux(wikiArticle)
                .flatMap(wikiService::parseMediaWikiTextFlux)
                .flatMap(parsedPage -> {
                    Flux<Integer> rating = ratingService.rateFlux(parsedPage);
                    Flux<Integer> wordCount = countService.countWordsFlux(parsedPage);

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
    public void combiningPublish() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        ConnectableFlux<ParsedPage> connectableFlux = wikiService.fetchArticleFlux(wikiArticle)
                .flatMap(wikiService::parseMediaWikiTextFlux)
                .publish();

        Flux<Integer> ratingFkux = connectableFlux.flatMap(ratingService::rateFlux);
        Flux<Integer> wordCountFlux = connectableFlux.flatMap(countService::countWordsFlux);
        connectableFlux.connect();

        ratingFkux.zipWith(wordCountFlux, (r, wc) -> String.format(
                "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                wikiArticle, r, wc))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }
}