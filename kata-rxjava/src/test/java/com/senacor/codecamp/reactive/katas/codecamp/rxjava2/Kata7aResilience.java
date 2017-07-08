package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import javax.swing.event.MouseInputListener;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1000));

        fetchArticleObservableWithTimeout(wikiService, "42")
                .subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Done"));
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

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(1));

        fetchArticleObservableWithTimeout(wikiService, "42");
    }

    @Test
    @KataClassification(nightmare)
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.
        WaitMonitor waitMonitor = new WaitMonitor();

        Function<Integer,Observable<String>> fetch = (delay) -> {
            WikiService wikiService = WikiService.create(DelayFunction.staticDelay(delay));
            return fetchArticleObservableWithTimeout(wikiService, "42").subscribeOn(Schedulers.io
                    ());
        };

        fetch.apply(1000).ambWith(fetch.apply(500)).ambWith(fetch.apply(200))
                .subscribe(x -> {
                    System.out.println(x);
                    waitMonitor.complete();
                });

        waitMonitor.waitFor(5, TimeUnit.SECONDS);
    }
}
