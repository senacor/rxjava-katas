package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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

        WikiService.WIKI_SERVICE.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS).subscribeOn(ReactiveUtil.newScheduler(1, "myScheduler"))
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                .flatMap(parsedPage -> zip(WikiService.WIKI_SERVICE.rate(parsedPage).subscribeOn(ReactiveUtil.newScheduler(1, "rateScheduler")),
                        WikiService.WIKI_SERVICE.countWords(parsedPage).subscribeOn(ReactiveUtil.newScheduler(1, "countScheduler")),
                        (rate, count) -> "{ name=\"" + parsedPage.getName() + ", rate=\"" + rate + "\", count=\"" + count + "\""))
                .observeOn(ReactiveUtil.newScheduler(1, "completeScheduler"))
                .subscribe(next -> System.out.println(String.format("%snext: " + next.toString(), ReactiveUtil.getThreadId())),
                        Throwable::printStackTrace,
                        () -> waitMonitor.complete());

        waitMonitor.waitFor(5000L, TimeUnit.MILLISECONDS);


    }

}
