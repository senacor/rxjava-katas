package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;

import org.wikipedia.Wiki;
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


        WaitMonitor waitMonitor = new WaitMonitor();

        Scheduler comp = ReactiveUtil.newScheduler(20, "comp");

        WIKI_SERVICE.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .take(20)
                .flatMap(articleName ->
                        WIKI_SERVICE.fetchArticle(articleName)
                                .subscribeOn(Schedulers.io()))
                .subscribeOn(comp)
                        .flatMap(pageText -> WIKI_SERVICE.parseMediaWikiText(pageText))
                        .flatMap(page -> {
                                    Observable<String> zip = Observable.zip( // ZZZZip
                                            WIKI_SERVICE.countWords(page),
                                            WIKI_SERVICE.rate(page),
                                            (c, r) -> "{ name: " + page.getName() + ", rate: " + r + ", wordcount: " + c + " }"
                                    );

                                    return zip;
                                }
                        ).subscribeOn(comp)
                        .subscribe(System.out::println,
                                Throwable::printStackTrace,
                                waitMonitor::complete);

        waitMonitor.waitFor(50, TimeUnit.SECONDS);

    }

}
