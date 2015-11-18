package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

import javax.tools.JavaCompiler;

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



        WikiService.WIKI_SERVICE.fetchArticle("Java").flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText).flatMap(s -> {
            Observable<Integer> rate = WikiService.WIKI_SERVICE.rate(s);

            Observable<Integer> words = WikiService.WIKI_SERVICE.countWords(s);

            return Observable.zip(words,rate, (wo,ra) -> {
               return String.format("ArticleName:Java, rating:%s, wordCount: %s",wo,ra);
            });

        }).subscribe(s -> {
              ReactiveUtil.print(s);
        });
    }

}
