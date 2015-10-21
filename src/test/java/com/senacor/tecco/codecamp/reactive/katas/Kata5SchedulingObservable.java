package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;
import static rx.Observable.zip;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    @Test
    public void schedulingObservable() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable,
        // der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
        // 2. nim nur die ersten 20 Artikel
        // 3. lade und parse die Artikel
        // 4. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"rating": 3, "wordCount": 452}
        // 5. messe die Laufzeit
        // 6. Füge jetzt an passender Stelle in der Observable-Chain einen Schduler ein um die Ausführungszeit zu verkürzen

        final WaitMonitor monitor = new WaitMonitor();
        WIKI_SERVICE.wikiArticleBeingReadObservable(150, TimeUnit.MILLISECONDS)
                .take(20)
                .flatMap(articleName -> WIKI_SERVICE.fetchArticle(articleName).subscribeOn(Schedulers.io()))
                .flatMap(articleString -> WIKI_SERVICE.parseMediaWikiText(articleString))
                .flatMap(article -> zip(WIKI_SERVICE.countWords(article), WIKI_SERVICE.rate(article),
                        (count, rate) -> String.format("{\"rating\": %s, \"wordCount\": %s}", rate, count)
                        ))
        .subscribe(result -> System.out.println(result),
                error -> error.printStackTrace(),
                monitor::complete);

        monitor.waitFor(15, TimeUnit.SECONDS);
    }

}
