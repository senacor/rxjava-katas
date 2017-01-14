package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.observers.Subscribers;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    private final WikiService wikiService = new WikiService();
    private final RatingService ratingService = new RatingService();
    private final CountService countService = new CountService();

    @Test
    public void schedulingObservableUnopt() throws Exception {
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 2. take only the first 20 articles
        // 3. load and parse the article
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Beispiel {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        TestSubscriber subscriber = TestSubscriber.create();

        long startTime = System.currentTimeMillis();

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .limit(20)
                .flatMap(articleName -> wikiService.fetchArticleObservable(articleName))
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> Observable.zip(ratingService.rateObservable(parsedPage),
                        countService.countWordsObervable(parsedPage),
                        (r, c) -> new String("{rating:" + r + ",count:" + c + "}")
                ))
                .doOnNext(x -> System.out.println(x))
                .doOnCompleted(() -> System.out.println("Total runtime: " + (System.currentTimeMillis() - startTime) + "ms"))
                .subscribe(subscriber);

        subscriber.awaitTerminalEvent(30, TimeUnit.SECONDS);
    }

    @Test
    public void schedulingObservable() throws Exception {
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 2. take only the first 20 articles
        // 3. load and parse the article
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Beispiel {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        TestSubscriber subscriber = TestSubscriber.create();

        long startTime = System.currentTimeMillis();

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .limit(20)
                .flatMap(articleName -> wikiService.fetchArticleObservable(articleName).subscribeOn(Schedulers.io()))
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> Observable.zip(ratingService.rateObservable(parsedPage)
                                .subscribeOn(Schedulers.computation()),
                        countService.countWordsObervable(parsedPage)
                                .subscribeOn(Schedulers.computation()),
                        (r, c) -> new String("{rating:" + r + ",count:" + c + "}")
                ))
                .doOnNext(x -> System.out.println(x))
                .doOnCompleted(() -> System.out.println("Total runtime: " + (System.currentTimeMillis() - startTime) + "ms"))
                .subscribe(subscriber);

        subscriber.awaitTerminalEvent(30, TimeUnit.SECONDS);
    }

}
