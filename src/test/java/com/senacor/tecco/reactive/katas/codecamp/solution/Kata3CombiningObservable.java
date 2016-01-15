package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static rx.Observable.zip;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private final WikiService wikiService = new WikiService();
    private final RatingService ratingService = new RatingService();
    private final CountService countService = new CountService();

    @Test
    public void combiningObservable() throws Exception {
        // 1. Wikiartikel holen und parsen
        // 2. Benutze jetzt den RatingService.rateObservable() und #CountService.countWordsObervable() und kombiniere beides im JSON-Format.
        //    Gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        wikiService.fetchArticleObservable(wikiArticle)
                .flatMap(wikiService::parseMediaWikiText)
                .flatMap(parsedPage -> {
                    Observable<Integer> rating = ratingService.rateObservable(parsedPage);
                    Observable<Integer> wordCount = countService.countWordsObervable(parsedPage);
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
