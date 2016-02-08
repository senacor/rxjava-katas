package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    private final WikiService wikiService = new WikiService();

    @Test
    public void errors() throws Exception {
        // 1. use WikiService#wikiArticleBeingReadObservableWithRandomErrors that creates a stream of wiki article names being read.
        // 2. filter burst.
        // 3. handle error: use retries with increasing delays
        // 4. if retries fail, terminate stream with a default

        final WaitMonitor monitor = new WaitMonitor();
        Subscription subscription = wikiService.wikiArticleBeingReadObservableWithRandomErrors()
                .debounce(50, TimeUnit.MILLISECONDS)
                .retryWhen(attempts -> attempts.zipWith(Observable.range(1, 2), (n, i) -> i)
                        .flatMap(i -> {
                            print("randomDelay retry by %s second(s)", i);
                            return Observable.timer(i, TimeUnit.SECONDS);
                        }))
                // TODO (ak) funktioniert noch nicht: 4. Falls die Retrys nicht helfen beende den Stream mit einem Default
                // TODO (ak) wenn die retries vorbei sind, dann ist der Aufruf erfolgreich und der default wird ignoriert
                .onErrorReturn(throwable -> "default on error")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(20, TimeUnit.SECONDS);
        subscription.unsubscribe();
    }

}
