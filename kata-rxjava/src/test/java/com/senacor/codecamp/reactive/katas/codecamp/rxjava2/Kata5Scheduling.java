package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata5Scheduling {

    private final WikiService wikiService = WikiService.create(DelayFunction.staticDelay(500), FlakinessFunction.noFlakiness(), true, "de");
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


        Scheduler thread1 = Schedulers.from(Executors.newFixedThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat("Thread1").build()));

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .take(20)
                .flatMap(a -> wikiService.fetchArticleObservable(a).subscribeOn(Schedulers.io()))
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> {
                        Observable<Integer> rating = ratingService.rateObservable(parsedPage);
                        Observable<Integer> wordCount = countService.countWordsObservable(parsedPage);
                        return Observable.zip(rating, wordCount, (r, wc) -> String.format(
                "{\"rating\": %s, \"wordCount\": %s}", r, wc)).subscribeOn(Schedulers.computation());
                })
                .subscribe(System.out::println,
                Throwable::printStackTrace,
                waitMonitor::complete);
        waitMonitor.waitFor(100, TimeUnit.SECONDS);
    }

}
