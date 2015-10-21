package com.senacor.tecco.codecamp.reactive.katas.solution;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.newScheduler;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;
import static rx.Observable.zip;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    @Test
    public void combiningObservable() throws Exception {
        // 1. Wikiartikel holen und parsen
        // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        Scheduler rateScheduler = newScheduler(3, "rate-scheduler");
        Scheduler countScheduler = newScheduler(3, "count-scheduler");
        Scheduler myScheduler = newScheduler(3, "my-scheduler");

        final String wikiArticle = "Bilbilis";
        WIKI_SERVICE.fetchArticle(wikiArticle)
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .flatMap(parsedPage -> {
                    Observable<Integer> rating = WIKI_SERVICE.rate(parsedPage);
                    Observable<Integer> wordCount = WIKI_SERVICE.countWords(parsedPage);
                    return zip(rating, wordCount, (r, wc) -> String.format(
                            "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                            wikiArticle, r, wc));
                })
                .subscribeOn(myScheduler)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

}
