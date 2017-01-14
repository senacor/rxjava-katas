package com.senacor.tecco.reactive.katas.codecamp;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import io.vertx.core.json.JsonObject;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executors;
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
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 2. take only the first 20 articles
        // 3. load and parse the article
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Beispiel {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        WaitMonitor waitMonitor = new WaitMonitor();

        Scheduler sch = ReactiveUtil.newScheduler(8, "exercise5");

        Subscription sub = wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
            .take(20)
            .flatMap(name -> wikiService.fetchArticleObservable(name).subscribeOn(Schedulers.io()))
            .map(article -> wikiService.parseMediaWikiText(article))
            .flatMap(text -> {

                Observable<Integer> rating = ratingService.rateObservable(text).subscribeOn(sch);
                Observable<Integer> counter = countService.countWordsObervable(text).subscribeOn(sch);

                return Observable.zip(counter, rating, (s1, s2) -> new JsonObject().put("rating", s1).put("wordCount", s2));
            })
            .subscribeOn(sch)
            .subscribe(
                n -> System.out.println("Article: " + n.encodePrettily()),
                err -> System.err.println("Error: " + err.getMessage()),
                () ->  {
                    waitMonitor.complete();
                    System.out.println("Done");
                }
            );

        waitMonitor.waitFor(15, TimeUnit.SECONDS);
        sub.unsubscribe();
    }
}
