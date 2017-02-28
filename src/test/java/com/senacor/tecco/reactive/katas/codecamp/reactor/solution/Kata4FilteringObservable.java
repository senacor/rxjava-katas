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
public class Kata4FilteringObservable {

    private final WikiService wikiService = new WikiService();

    @Test
    public void filterObservable() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. Filter the names so that only articles with at least 15 characters are accepted and print everything to the console

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

    @Test
    public void filterObservable2() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers to many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)

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
