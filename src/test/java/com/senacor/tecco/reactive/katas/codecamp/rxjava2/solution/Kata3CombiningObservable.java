package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    @Test
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and #countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        Observable<ParsedPage> pages = wikiService.fetchArticleObservable(wikiArticle)
                                                  .flatMap(wikiService::parseMediaWikiTextObservable);

        Observable<Integer> rating = pages.flatMap(ratingService::rateObservable);
        Observable<Integer> wordCount = pages.flatMap(countService::countWordsObservable);

        rating.zipWith(wordCount, (r, wc) -> String.format(
                "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                wikiArticle, r, wc))
              .subscribe(next -> print("next: %s", next),
                      Throwable::printStackTrace,
                      waitMonitor::complete);

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

    @Test
    public void combiningObservablePublish() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and #countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        ConnectableObservable<ParsedPage> parsedPageObservable = wikiService.fetchArticleObservable(wikiArticle)
                                                                            .flatMap(wikiService::parseMediaWikiTextObservable)
                                                                            .publish();

        Observable<Integer> rating = parsedPageObservable.flatMap(ratingService::rateObservable);
        Observable<Integer> wordCount = parsedPageObservable.flatMap(countService::countWordsObservable);
        parsedPageObservable.connect();

        rating.zipWith(wordCount, (r, wc) -> String.format(
                "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                wikiArticle, r, wc))
              .subscribe(next -> print("next: %s", next),
                      Throwable::printStackTrace,
                      () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

}
