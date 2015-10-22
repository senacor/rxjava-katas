package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;
import rx.Scheduler;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.newScheduler;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;
import static rx.Observable.zip;

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

        final WaitMonitor waitMonitor = new WaitMonitor();
        final Scheduler scheduler = newScheduler(3, "main-scheduler");
        final Scheduler helpScheduler = newScheduler(50, "help-scheduler");

        WIKI_SERVICE.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .take(20)
                .flatMap(name -> WIKI_SERVICE.fetchArticle(name).subscribeOn(helpScheduler))
                .flatMap(article -> WikiService.WIKI_SERVICE.parseMediaWikiText(article).subscribeOn(helpScheduler))
                .flatMap(parsedPage -> zip(
                                // Separates scheduling fuer jeden Observable, da sonst keine Verteilung
                                WIKI_SERVICE.rate(parsedPage).subscribeOn(helpScheduler),
                                WIKI_SERVICE.countWords(parsedPage).subscribeOn(helpScheduler),
                                (s1, s2) -> {
                                    final String json  = "{rating:" + s1 + ", wordCount:" + s2 + "}";
                                    System.out.println("Function: " + ReactiveUtil.getThreadId());
                                    return json;})

                )
                // Generelle Scheduler/Pool Zuordnung -> wirkt sich zB. auf fetch/parse aus
                //.subscribeOn(scheduler)
                .subscribe(
                        json -> System.out.println("json: " + ReactiveUtil.getThreadId() + " : " + json),
                        Throwable::printStackTrace,
                        waitMonitor::complete
                );

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

}
