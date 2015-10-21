package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private static WikiService wikiService = new WikiService();

    @Test
    public void combiningObservable() throws Exception {
        // 1. Wikiartikel holen und parsen

        String articleName = "observable";
        Observable<String> article = wikiService.fetchArticle(articleName);
        Observable<ParsedPage> page = article.flatMap(text -> wikiService.parseMediaWikiText(text));

        // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format

        Observable<Integer> count = page.flatMap(item -> wikiService.countWords(item));
        Observable<Integer> rate = page.flatMap(item -> wikiService.rate(item));

        //    und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

        Observable.zip(rate
                , count
                , (rating, wordCount) ->
                String.format("{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}", articleName, rating, wordCount))
                .subscribe(res -> System.out.println(res));

        // WikiService.WIKI_SERVICE.fetchArticle()
    }

}
