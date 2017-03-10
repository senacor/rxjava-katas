package com.senacor.codecamp.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.codecamp.reactive.services.PersistService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    private final WikiService wikiService = WikiService.create();
    private final PersistService persistService = PersistService.create();

    @Test
    public void withoutBatch() throws Exception {
        // 1. use WikiService#wikiArticleBeingReadObservableBurst that returns a stream of wiki article being read
        // 2. watch the stream 2 sec
        // 3. save the article (WikiService.save(String)). The service returns the execution time
        // 4. sum the execution time of the service calls and print the result

        wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .doOnNext(ReactiveUtil::print)
                .map(persistService::save)
                .reduce((l, r) -> l + r)
                .map(runtime -> String.format("save runtime (SUM): %s ms", runtime))
                .doOnSuccess(ReactiveUtil::print)
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertComplete();
    }


    @Test
    public void batch() throws Exception {
        // 1. do the same as above, but this time use the method #save(Iterable) to save a batch of articles.
        //    use a batch size of 5.
        //    Please note that this is a stream - you can not wait until all articles are delivered to save everything in a batch
        // 2. If the batch size is not reached within 500 milliseconds,
        //    flush the buffer anyway by writing to the service

        wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .doOnNext(ReactiveUtil::print)
                .buffer(5, 500, TimeUnit.MILLISECONDS)
                .map(persistService::save)
                .reduce((l, r) -> l + r)
                .map(runtime -> String.format("save runtime (SUM): %s ms", runtime))
                .doOnSuccess(ReactiveUtil::print)
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertComplete();
    }
}
