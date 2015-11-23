package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.ReactiveUtil;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    @Test
    public void schedulingObservable() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
        // 2. nim nur die ersten 20 Artikel
        // 3. lade und parse die Artikel
        // 4. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"rating": 3, "wordCount": 452}
        // 5. messe die Laufzeit
        // 6. Füge jetzt an passender Stelle in der Observable-Chain einen Scheduler ein um die Ausführungszeit zu verkürzen

        final WaitMonitor monitor = new WaitMonitor();

        Scheduler fiveThreads = ReactiveUtil.newScheduler(50, "my-scheduler");

        Subscription subscription = WikiService.WIKI_SERVICE.wikiArticleBeingReadObservable(1, TimeUnit.MILLISECONDS)
                .take(20)//.observeOn(Schedulers.io())
                .flatMap(name -> WikiService.WIKI_SERVICE.fetchArticle(name).subscribeOn(Schedulers.io()))
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                .flatMap(parsedPage -> Observable.zip(WikiService.WIKI_SERVICE.rate(parsedPage).subscribeOn(fiveThreads),
                        WikiService.WIKI_SERVICE.countWords(parsedPage).subscribeOn(fiveThreads),
                        (rating, wordCount) -> String.format(
                                "{\"rating\": %s, \"wordCount\": %s}",
                                rating, wordCount)))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(10, TimeUnit.SECONDS);
        subscription.unsubscribe();
    }

}
