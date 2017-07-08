package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;

/**
 * @author Andreas Keefer
 */
public class Kata7aResilience {

    @Test
    @KataClassification(mandatory)
    public void timeout() throws Exception {
        // 1. use fetchArticleObservableWithTimeout and add there a timeout of 500ms.
        // 2. verify this behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400));

        fetchArticleObservableWithTimeout(wikiService, "42")
        .test()
        .assertComplete();
    }

    private Observable<String> fetchArticleObservableWithTimeout(WikiService wikiService, String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .timeout(500, TimeUnit.MILLISECONDS);
    }

    @Test
    @KataClassification(hardcore)
    public void retry() throws Exception {
        // 3. when fetchArticleObservableWithTimeout fails, retry twice with a delay of 1 second
        // 4. verify this behavior with tests

        WaitMonitor waitMonitor = new WaitMonitor();

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(3));

        fetchArticleObservableWithTimeout(wikiService, "42")
                .retryWhen(attempts -> attempts.zipWith(Observable.range(1, 2), (n, i) -> i).flatMap(i -> Observable.timer(i, TimeUnit.SECONDS)))
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        waitMonitor::complete);
        waitMonitor.waitFor(100, TimeUnit.SECONDS);
    }

    @Test
    @KataClassification(nightmare)
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.

        WikiService wikiService = WikiService.create(DelayFunction.withRandomDelay(200, 1000));

        fetchArticleObservableWithTimeout(wikiService, "42")
                .ambWith(fetchArticleObservableWithTimeout(wikiService, "42").delay(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()))
                .ambWith(fetchArticleObservableWithTimeout(wikiService, "42").delay(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .doOnNext((System.out::println))
                .test()
                .awaitDone(10, TimeUnit.SECONDS);
    }
}
