package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
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

        // WikiService.WIKI_SERVICE.fetchArticle()
        Observable<ParsedPage> parsedPageObservable = parsedArticle("Russland");
        parsedPageObservable.flatMap(parsedPage -> {
            Observable<Integer> rating = WikiService.WIKI_SERVICE.rate(parsedPage);
            Observable<Integer> wordCount = WikiService.WIKI_SERVICE.countWords(parsedPage);
            return Observable.zip(rating, wordCount, (r, w) ->
                 "{\"rating\": " + r + ", \"wordCount\": " + w + "}"
            );
        }).forEach(o -> System.out.println(o));
    }


    private Observable<ParsedPage> parsedArticle(String name) {
        return WikiService.WIKI_SERVICE.fetchArticle(name)
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                ;
    }

}
