package com.senacor.tecco.codecamp.reactive.katas;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    @Test
    public void combiningObservable() throws Exception {
        // 1. Wikiartikel holen und parsen
        // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}


        Observable<ParsedPage> article = WIKI_SERVICE
                .fetchArticle("Java")
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .cache();

        Observable<Integer> rating = article.flatMap(WIKI_SERVICE::rate);
        Observable<Integer> wordCount = article.flatMap(WIKI_SERVICE::countWords);

        Observable<String> result = Observable.zip(article, rating, wordCount,
                (a, r, wc) -> String.format("Article: %s, Rating: %s, Word Count %s", a.getText(), r, wc));

        result.subscribe(System.out::println, e -> e.printStackTrace(), System.out::println);

        // WikiService.WIKI_SERVICE.fetchArticle()
    }

}
