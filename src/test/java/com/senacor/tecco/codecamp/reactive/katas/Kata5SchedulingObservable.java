package com.senacor.tecco.codecamp.reactive.katas;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    @Test
    public void schedulingObservable() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
        // 2. nim nur die ersten 20 Artikel
        // 3. lade und parse die Artikel
        // 4. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"rating": 3, "wordCount": 452}
        // 5. messe die Laufzeit
        // 6. Füge jetzt an passender Stelle in der Observable-Chain einen Schduler ein um die Ausführungszeit zu verkürzen

        WaitMonitor waitMonitor = new WaitMonitor();

        WIKI_SERVICE.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS) //
            .take(20) //
            .flatMap(articleName -> Observable.zip( //
                Observable.just(articleName), //
                WIKI_SERVICE.fetchArticle(articleName).subscribeOn(Schedulers.io()), //
                NamedArticle::new)) //
            .doOnNext(debug -> ReactiveUtil.print("articleWithName: %s", debug)).flatMap(articleWithName -> {
            final Observable<ParsedPage> parsedPage = WIKI_SERVICE.parseMediaWikiText(articleWithName.article);
            return parsedPage.zipWith( //
                Collections.singletonList(articleWithName.name), //
                (page, name) -> {
                    page.setName(name);
                    return page;
                });
        }) //
            .flatMap(parsedPage -> { //
                final Observable<Integer> rateObservable = WIKI_SERVICE.rate(parsedPage);
                final Observable<Integer> countWordsObservable = WIKI_SERVICE.countWords(parsedPage);
                return Observable
                    .zip(rateObservable, //
                        countWordsObservable, //
                        (rate, countWords) -> new ArticleInfos(parsedPage.getName(), rate, countWords)); //
            }).subscribe(articleInfos -> { //
            ReactiveUtil
                .print("{\"articleName\": \"%s\", \"rating\": %d, \"wordCount\": %d}", articleInfos.name, articleInfos.rate, articleInfos.count);
        }, ReactiveUtil::print, waitMonitor::complete);

        waitMonitor.waitFor(30, TimeUnit.SECONDS);
    }

    private class NamedArticle {
        private final String name;
        private final String article;

        public NamedArticle(String name, String article) {
            this.name = name;
            this.article = article;
        }
    }

    private class ArticleInfos {
        private final String name;
        private final Integer rate;
        private final Integer count;

        private ArticleInfos(String name, Integer rate, Integer count) {
            this.name = name;
            this.rate = rate;
            this.count = count;
        }
    }
}
