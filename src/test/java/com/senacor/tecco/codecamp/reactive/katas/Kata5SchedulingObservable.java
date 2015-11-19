package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    @Test
    public void schedulingObservable() throws Exception {
        final long start = System.currentTimeMillis();

        WaitMonitor monitor = new WaitMonitor();
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
//        WIKI_SERVICE.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
        Observable.from(Arrays.asList("Berlin", "Moskau", "Wien", "London", "Paris"))
        // 2. nim nur die ersten 20 Artikel
//        .take(20)
        // 3. lade und parse die Artikel
        .flatMap((wikiArticle) -> WIKI_SERVICE.fetchArticle(wikiArticle).subscribeOn(myScheduler()))
        .flatMap(WIKI_SERVICE::parseMediaWikiText)
        // 4. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"rating": 3, "wordCount": 452}
         .flatMap(parsedPage -> {
             Observable<Integer> rating = WikiService.WIKI_SERVICE.rate(parsedPage).subscribeOn(myScheduler());
             Observable<Integer> wordCount = WikiService.WIKI_SERVICE.countWords(parsedPage).subscribeOn(myScheduler());
             return Observable.zip(rating, wordCount, (r, w) ->
                             "{\"rating\": " + r + ", \"wordCount\": " + w + "}"
             );
         }).subscribe(out -> ReactiveUtil.print(out),
                        e -> e.printStackTrace(),
                        () -> monitor.complete()
                );
        // 5. messe die Laufzeit

        monitor.waitFor(30, TimeUnit.SECONDS);

        // 6. Füge jetzt an passender Stelle in der Observable-Chain einen Schduler ein um die Ausführungszeit zu verkürzen

        Observable<String> observable = WIKI_SERVICE.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS);
    }

    private Scheduler myScheduler() {
        return ReactiveUtil.newScheduler(50,"MyScheduler");
    }

}
