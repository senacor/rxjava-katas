package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata4Filtering {

    private final WikiService wikiService = WikiService.create();

    /**
     * 1. Use {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} that delivers a stream of wiki article
     * names being read
     * 2. Filter the names so that only articles with at least 15 characters long names are accepted and print
     * everything to the console
     */
    @Test
    public void filter() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Flux<String> articles = wikiService.wikiArticleBeingReadFlux(500, TimeUnit.MILLISECONDS);
        Disposable subscription = articles
                .filter(name -> name.length() >= 15)
                .subscribe(next -> print("PASS THROUGH: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(10, TimeUnit.SECONDS);
        subscription.dispose();
    }

    /**
     * 1. Use  {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} that delivers a stream of
     * wiki article names being read
     * 2. The stream delivers to many article to be processed.
     * Limit the stream to one article in 500ms. Do not change the parameter
     * at {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} ;)
     */
    @Test
    public void filter2() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Flux<String> articles = wikiService.wikiArticleBeingReadFlux(100, TimeUnit.MILLISECONDS);
        Disposable subscription = articles
                .sample(Duration.of(500, ChronoUnit.MILLIS))
                .subscribe(next -> print("PASS THROUGH: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(5, TimeUnit.SECONDS);
        subscription.dispose();
    }
}