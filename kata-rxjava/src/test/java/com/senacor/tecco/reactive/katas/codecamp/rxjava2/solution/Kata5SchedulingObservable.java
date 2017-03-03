package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.util.ReactiveUtil;
import com.senacor.tecco.reactive.util.WaitMonitor;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    @Test
    public void schedulingObservable() throws Exception {
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 2. take only the first 20 articles
        // 3. load and parse the article
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Beispiel {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        final WaitMonitor monitor = new WaitMonitor();

        Scheduler fiveThreads = ReactiveUtil.newRxScheduler(5, "my-scheduler");

        Disposable subscription = wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .take(20)
                .flatMap(name -> wikiService.fetchArticleObservable(name).subscribeOn(Schedulers.io()))
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> Observable.zip(ratingService.rateObservable(parsedPage).subscribeOn(fiveThreads),
                        countService.countWordsObservable(parsedPage).subscribeOn(fiveThreads),
                        (rating, wordCount) -> String.format(
                                "{\"rating\": %s, \"wordCount\": %s}",
                                rating, wordCount)))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(10, TimeUnit.SECONDS);
        subscription.dispose();
    }

}
