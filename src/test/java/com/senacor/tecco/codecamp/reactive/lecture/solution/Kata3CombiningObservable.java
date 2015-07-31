package com.senacor.tecco.codecamp.reactive.lecture.solution;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    @Test
    public void combiningObservable() throws Exception {
        // 1. Wikiartikel holen und parsen
        // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"rating": 3, "wordCount": 452}

        final String wikiArticle = "Bilbilis";
        WIKI_SERVICE.fetchArticle(wikiArticle)
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .flatMap(parsedPage -> Observable.zip(WIKI_SERVICE.rate(parsedPage),
                        WIKI_SERVICE.countWords(parsedPage),
                        (rating, wordCount) -> String.format(
                                "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                                wikiArticle, rating, wordCount)))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

}
