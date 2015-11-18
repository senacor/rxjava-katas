package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

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
        Scheduler fiveThreads = ReactiveUtil.newScheduler(50, "my-scheduler");

        Observable<String> articleNames = WIKI_SERVICE.wikiArticleBeingReadObservable(1, TimeUnit.MILLISECONDS).take(20);

        Observable<ParsedPage> articles = articleNames
                .flatMap(WIKI_SERVICE::fetchArticle)
                .subscribeOn(Schedulers.io())
                .flatMap(WIKI_SERVICE::parseMediaWikiText).cache();

        Observable<Integer> ratings = articles.flatMap(WIKI_SERVICE::rate).subscribeOn(fiveThreads);
        Observable<Integer> wordCounts = articles.flatMap(WIKI_SERVICE::countWords).subscribeOn(fiveThreads);

        Observable<String> results = Observable.zip(articles, ratings, wordCounts,
                (a, r, wc) -> String.format("Article: %s, Rating: %s, Word Count %s", StringUtils.left(a.getText(), 20), r, wc));

        results.subscribe(System.out::println, e -> e.printStackTrace(), System.out::println);

        waitMonitor.waitFor(20, TimeUnit.SECONDS);
    }

}
