package com.senacor.tecco.codecamp.reactive.katas.solution;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    @Test
    public void errors() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservableWithRandomErrors, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden.
        // 2. Filtere zuerst die Bursts heraus.
        // 3. Behandle auftretende Fehler: Versuche zuerst einen paar Retrys mit steigender pause dazwischen
        // 4. Falls die Retrys nicht helfen beende den Stream mit einem Default

        final WaitMonitor monitor = new WaitMonitor();
        Subscription subscription = WikiService.WIKI_SERVICE.wikiArticleBeingReadObservableWithRandomErrors()
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
