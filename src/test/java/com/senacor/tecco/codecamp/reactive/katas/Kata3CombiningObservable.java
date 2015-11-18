package com.senacor.tecco.codecamp.reactive.katas;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
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
        final String articleName = "Abrollrichtung_von_Toilettenpapier";

        final Observable<ParsedPage> articleObservable = WIKI_SERVICE.fetchArticle(articleName)
            .doOnNext(debug -> print("fetchArticle res: %s", debug))
            .flatMap(WIKI_SERVICE::parseMediaWikiText)
            .doOnNext(debug -> print("parseArticle res: %s", debug));

        articleObservable.flatMap(WIKI_SERVICE::rate)
            .doOnNext(debug -> print("rate: %d", debug))
            .zipWith(articleObservable.flatMap(WIKI_SERVICE::countWords).doOnNext(debug -> print("count: %d", debug)), ArticleInfos::new)
            .doOnNext(debug -> print("zipping: %s", debug))
            .subscribe(articleInfos -> {
                ReactiveUtil
                    .print("{\"articleName\": \"%s\", \"rating\": %d, \"wordCount\": %d}", articleName, articleInfos.rate, articleInfos.count);
            }, ReactiveUtil::print);
    }

    private class ArticleInfos {
        private final Integer rate;
        private final Integer count;

        private ArticleInfos(Integer rate, Integer count) {
            this.rate = rate;
            this.count = count;
        }
    }

}
