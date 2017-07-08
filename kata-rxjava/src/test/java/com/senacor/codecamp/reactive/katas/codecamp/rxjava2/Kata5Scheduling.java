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
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 2. take only the first 20 articles
        // 3. load and parse the article
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        WaitMonitor waitMonitor = new WaitMonitor();

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .take(20)
                .flatMap(s -> wikiService.fetchArticleObservable(s).subscribeOn(Schedulers.io()))
                .map(wikiService::parseMediaWikiText)
                .flatMap(parsedPage -> {
                    Observable<Integer> rateObservable = ratingService.rateObservable(parsedPage);
                    Observable<Integer> countObservable = countService.countWordsObservable(parsedPage);
                    return rateObservable.zipWith(countObservable,
                            (rating, count) -> String.format("{\"rating\": %d, \"wordCount\": %d}", rating, count)
                    );
                })
                .subscribe(System.out::println, System.err::println, waitMonitor::complete);

        waitMonitor.waitFor(20, TimeUnit.SECONDS);
    }

}
