package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.observables.JoinObservable;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    private final WikiService wikiService = new WikiService();
    private final RatingService ratingService = new RatingService();
    private final CountService countService = new CountService();

    @Test
    public void schedulingObservable() throws Exception {

        int nArticles = 20;
        long startMillis = System.currentTimeMillis();
        CyclicBarrier barrier = new CyclicBarrier(nArticles,
                () -> {
                    long totalMillis = System.currentTimeMillis() - startMillis;
                    System.out.println("Execution time: " + totalMillis);
                });

        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
        // 2. take only the first 20 articles
            .take(nArticles)
        // 3. load and parse the article
            .flatMap(x -> wikiService.parseMediaWikiTextObservable(x))
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Beispiel {"rating": 3, "wordCount": 452}
            .flatMap( x -> {
            Observable<Integer> ratings = ratingService.rateObservable(x);
            Observable<Integer> counts = countService.countWordsObervable(x);
            return JoinObservable.when(
                    JoinObservable.from(ratings)
                            .and(counts)
                            .then((rate, count) -> "rating: " + rate + ", wordCount:" + count)
            ).toObservable();
        })

        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time


        .subscribe(
                x -> {
                    System.out.println(x);
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace,
                () -> System.out.println("done")
        );

        barrier.await();
    }

}
