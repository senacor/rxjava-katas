package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import io.reactivex.Observable;
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

        long start = System.nanoTime();

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .take(20)
                .map(wikiService::fetchArticle)
                .map(wikiService::parseMediaWikiText)
                .flatMap(page -> {
                    Observable<Integer> ratings = ratingService.rateObservable(page);
                    Observable<Integer> wordCounts = countService.countWordsObservable(page);
                    return Observable.zip(ratings, wordCounts, (r, wc) -> String.format("{\"rating\": %s, " +
                            "\"wordCount\": %s}", r, wc));
                })
                .doOnNext(System.out::println)
                .test()
                .awaitDone(10, TimeUnit.SECONDS);

        long end = System.nanoTime();

        System.out.printf("Took %s s\n", (end - start) * 1e-9);
    }

}
