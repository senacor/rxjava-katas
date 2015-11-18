package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

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
        // 6. Füge jetzt an passender Stelle in der Observable-Chain einen Schduler ein um die Ausführungszeit zu verkürzen


        Scheduler myScheduler = ReactiveUtil.newScheduler(5, "myScheduler");

        final WaitMonitor monitor = new WaitMonitor();
        WIKI_SERVICE.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS).take(20)
        .flatMap(WIKI_SERVICE::fetchArticle).subscribeOn(myScheduler).
                flatMap(WIKI_SERVICE::parseMediaWikiText)
                .flatMap(page -> {
                    Observable<Integer> countedWords = WIKI_SERVICE.countWords(page).subscribeOn(myScheduler);
                    Observable<Integer> pageRate = WIKI_SERVICE.rate(page).subscribeOn(myScheduler);

                    return Observable.zip(countedWords, pageRate, (wo, ra) -> {
                        return String.format("ArticleName:Java, rating:%s, wordCount: %s", wo, ra);
                    });
                }).subscribeOn(myScheduler).subscribe(json -> {
            ReactiveUtil.print(json);
        }, Throwable::printStackTrace, () -> monitor.complete());

        monitor.waitFor(10, TimeUnit.SECONDS);
    }

}
