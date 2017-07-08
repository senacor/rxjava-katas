package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.PersistService;
import com.senacor.codecamp.reactive.services.WikiService;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    private final WikiService wikiService = WikiService.create();
    private final PersistService persistService = PersistService.create();

    @Test
    @KataClassification(KataClassification.Classification.mandatory)
    public void withoutBatch() throws Exception {

        // 1. use WikiService#wikiArticleBeingReadObservableBurst that returns a stream of wiki article being read
        // 2. watch the stream 2 sec
        // 3. save the article (PersistService.save(String)). The service returns the execution time
        // 4. sum the execution time of the service calls and print the result

        wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, SECONDS)
                .map(persistService::save)
                .reduce(0L, Long::sum)
                .doOnSuccess(a -> System.out.println("Write time: " + a + "ms"))
                .subscribeOn(Schedulers.io())
                .test()
                .awaitDone(3, SECONDS);
    }


    @Test
    @KataClassification(KataClassification.Classification.mandatory)
    public void batch() throws Exception {
        // 1. do the same as above, but this time use the method #save(Iterable) to save a batch of articles.
        //    use a batch size of 5.
        //    Please note that this is a stream - you can not wait until all articles are delivered to save everything in a batch

        wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, SECONDS)
                .buffer(100)
                .map(persistService::save)
                .reduce(0L, Long::sum)
                .doOnSuccess(a -> System.out.println("Write time: " + a + "ms"))
                .subscribeOn(Schedulers.io())
                .test()
                .awaitDone(3, SECONDS);
    }

    @Test
    @KataClassification(KataClassification.Classification.advanced)
    public void batch2() throws Exception {
        // 2. If the batch size is not reached within 500 milliseconds,
        //    flush the buffer anyway by writing to the service

        wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, SECONDS)
                .buffer(500, MILLISECONDS, 15)
                .filter(a -> !a.isEmpty())
                .doOnNext(a -> System.out.println(a.size()))
                .map(persistService::save)
                .reduce(0L, Long::sum)
                .doOnSuccess(a -> System.out.println("Write time: " + a + "ms"))
                .subscribeOn(Schedulers.io())
                .test()
                .awaitDone(3, SECONDS);
    }
}
