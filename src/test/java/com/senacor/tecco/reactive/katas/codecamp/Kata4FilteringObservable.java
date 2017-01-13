package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.observers.TestSubscriber;
import rx.subjects.TestSubject;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata4FilteringObservable {

    private final WikiService wikiService = new WikiService();

    @Test
    public void filterObservable() throws Exception {
        TestSubscriber<String> test;

        wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
                .filter(name -> name.length() >= 15)
                .doOnNext(System.out::println)
                .subscribe(test = TestSubscriber.create());

        test.awaitTerminalEvent(5, TimeUnit.SECONDS);
    }

    @Test
    public void filterObservable2() throws Exception {
        TestSubscriber<String> test;

        wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .sample(500, TimeUnit.MILLISECONDS)
                .doOnNext(System.out::println)
                .subscribe(test = TestSubscriber.create());

        test.awaitTerminalEvent(5, TimeUnit.SECONDS);
    }
}
