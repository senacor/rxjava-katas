package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.PersistService;
import com.senacor.tecco.reactive.services.WikiService;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

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

        final WaitMonitor monitor = new WaitMonitor();

        Disposable subscribe = wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .doOnNext(ReactiveUtil::print)
                .map(persistService::save)
                .reduce((l, r) -> l + r)
                .subscribe(next -> print("save runtime (SUM): %s ms", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(10000, TimeUnit.MILLISECONDS);
        subscribe.dispose();
    }


    @Test
    public void batch() throws Exception {
        // 1. do the same as above, but this time use the method #save(Iterable) to save a batch of articles.
        //    Please note that this is a stream - you can not wait until all articles are delivered to save everything in a batch


        final WaitMonitor monitor = new WaitMonitor();

        Disposable subscribe = wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .doOnNext(ReactiveUtil::print)
                .buffer(500, TimeUnit.MILLISECONDS)
                .map(persistService::save)
                .reduce((l, r) -> l + r)
                .subscribe(next -> print("save runtime (SUM): %s ms", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(10000, TimeUnit.MILLISECONDS);
        subscribe.dispose();
    }
}
