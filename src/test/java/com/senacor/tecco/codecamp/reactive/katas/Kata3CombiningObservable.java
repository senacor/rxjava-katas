package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;
import org.wikipedia.Wiki;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    @Test
    public void combiningObservable() throws Exception {
        // 1. Wikiartikel holen und parsen
        // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WikiService.WIKI_SERVICE.fetchArticle("Bananen")
                .flatMap(article -> WikiService.WIKI_SERVICE.parseMediaWikiText(article))
                .flatMap(page -> {
                    String name = page.getName();

                    Observable<String> stringObservable = Observable.zip(
                            WikiService.WIKI_SERVICE.rate(page),
                            WikiService.WIKI_SERVICE.countWords(page),
                            (rO, iO) -> "{ name: " + page.getName() + ", rate: " + rO + ", wordcount: " + iO + " }");

                    return stringObservable;
                }).subscribe(ReactiveUtil::print);

    }

}
