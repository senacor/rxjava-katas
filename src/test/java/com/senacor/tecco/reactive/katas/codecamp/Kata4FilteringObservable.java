package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata4FilteringObservable {

    private final WikiService wikiService = new WikiService();

    @Test
    public void filterObservable() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. Filter the names so that only articles with at least 15 characters long names are accepted and print everything to the console

        TestSubscriber subscriber = TestSubscriber.create();

        wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
                .filter(x -> x.length() >= 15)
                .doOnNext(System.out::println)
                .subscribe(subscriber);

        subscriber.awaitTerminalEvent(30, TimeUnit.SECONDS);
    }

    @Test
    public void filterObservable2() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers too many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)

        TestSubscriber subscriber = TestSubscriber.create();

        wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .sample(500, TimeUnit.MILLISECONDS)
                .doOnNext(System.out::println)
                .subscribe(subscriber);

        subscriber.awaitTerminalEvent(30, TimeUnit.SECONDS);
    }
}
