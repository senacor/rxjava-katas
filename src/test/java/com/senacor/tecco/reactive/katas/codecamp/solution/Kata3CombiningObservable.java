package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.newScheduler;
import static com.senacor.tecco.reactive.ReactiveUtil.print;
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

        final String wikiArticle = "Bilbilis";
        WikiService.WIKI_SERVICE.fetchArticle(wikiArticle)
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                .flatMap(parsedPage -> {
                    Observable<Integer> rating = WikiService.WIKI_SERVICE.rate(parsedPage);
                    Observable<Integer> wordCount = WikiService.WIKI_SERVICE.countWords(parsedPage);
                    return zip(rating, wordCount, (r, wc) -> String.format(
                            "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                            wikiArticle, r, wc));
                })
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

}
