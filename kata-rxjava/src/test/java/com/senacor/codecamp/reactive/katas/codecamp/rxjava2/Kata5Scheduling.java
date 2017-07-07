package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata5Scheduling {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    @Test
    @KataClassification(KataClassification.Classification.mandatory)
    public void schedulingObservable() throws Exception {
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        WaitMonitor waitMonitor = new WaitMonitor();

        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 5. measure the runtime
        // First run: 11355 ms
        // Threaded run: 5188 ms
        // With mocks + threads: 2198ms
        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                // 2. take only the first 20 articles
                .take(20)
                .flatMap(s -> wikiService.fetchArticleObservable(s).subscribeOn(Schedulers.io()))
                // 3. load and parse the article
                .map(wikiService::parseMediaWikiText)
                // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
                //    and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
                .flatMap(
                        parsedPage -> {
                            Observable rating = ratingService.rateObservable(parsedPage).subscribeOn(Schedulers.computation());
                            Observable wordCount = countService.countWordsObservable(parsedPage).observeOn(Schedulers.computation());
                            return Observable.zip(rating, wordCount,
                                    (r, c) -> String.format("{\"rating\": %s, \"wordCount\": %s}", r, c)).subscribeOn(Schedulers.computation());
                        })
                .subscribe(System.out::println,
                        (e) -> System.err.println(e.getLocalizedMessage()),
                        waitMonitor::complete
                );

        waitMonitor.waitFor(20, TimeUnit.SECONDS);
    }
}
