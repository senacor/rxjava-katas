package com.senacor.tecco.codecamp.reactive.lecture.solution;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.observables.MathObservable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    @Test
    public void withoutBatch() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservableBurst, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden.
        // 2. beobachte den Stream 2 sekunden lang
        // 3. speichere die Artikel ab (WikiService.save(String)). Der service gibt die laufzeit zurück
        // 4. summiere die laufzeit der save calls und gib dies aus

        final WaitMonitor monitor = new WaitMonitor();

        Observable<Long> observable = WIKI_SERVICE.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .doOnNext(ReactiveUtil::print)
                .map(WIKI_SERVICE::save)
                .doOnNext(runtime -> print("runtime: %s ms", runtime));
        Subscription subscription = MathObservable.sumLong(observable)
                .subscribe(next -> print("save runtime (SUM): %s ms", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(10000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }


    @Test
    public void batch() throws Exception {
        // 1. mache das gleiche wie oben, nur verwende diesmal die #save(Iterable) Methode um einene Batch von
        //    Artikeln zu speichern. Beachte dabei, das du hier potentiel einen Stream hast, du kannst also nicht warten
        //    bis der Stream alle artikel geliefert hat und dann alles in einem grossen batch abspeichern

        final WaitMonitor monitor = new WaitMonitor();

        Observable<Long> observable = WIKI_SERVICE.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .doOnNext(ReactiveUtil::print)
                .buffer(500, TimeUnit.MILLISECONDS)
                .map(WIKI_SERVICE::save)
                .doOnNext(runtime -> ReactiveUtil.print("runtime: %sms", runtime));
        Subscription subscription = MathObservable.sumLong(observable)
                .subscribe(next -> print("save runtime: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(10000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }
}
