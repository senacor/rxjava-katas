package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.PersistService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    private final WikiService wikiService = new WikiService();
    private final PersistService persistService = new PersistService();

    @Test
    public void withoutBatch() throws Exception {
        // 1. use WikiService#wikiArticleBeingReadObservableBurst that returns a stream of wiki article being read
        // 2. watch the stream 2 sec
        // 3. save the article (WikiService.save(String)). The service returns the execution time
        // 4. sum the execution time of the service calls and print the result
        TestSubscriber test = TestSubscriber.create();

        wikiService.wikiArticleBeingReadObservableBurst()
                .takeUntil(Observable.timer(2, TimeUnit.SECONDS))
                .map(persistService::save)
                .scan((a, b) -> a+b)
                .doOnNext(System.out::println)
                .subscribe(test);

        test.awaitTerminalEvent();
    }


    @Test
    public void batch() throws Exception {
        // 1. do the same as above, but this time use the method #save(Iterable) to save a batch of articles.
        //    Please note that this is a stream - you can not wait until all articles are delivered to save everything in a batch

        TestSubscriber test = TestSubscriber.create();

        wikiService.wikiArticleBeingReadObservableBurst()
                .takeUntil(Observable.timer(2, TimeUnit.SECONDS))
                .buffer(2010, TimeUnit.MILLISECONDS)
                .map(persistService::save)
                .doOnNext(System.out::println)
                .subscribe(test);

        test.awaitTerminalEvent();
    }
}
