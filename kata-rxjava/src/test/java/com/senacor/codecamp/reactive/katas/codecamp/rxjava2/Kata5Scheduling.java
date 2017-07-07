package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import javax.management.monitor.Monitor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.err;

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

        WaitMonitor monitor = new WaitMonitor();
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)

        // 2. take only the first 20 articles
            .take(20)
              //  .observeOn(Schedulers.io())
                .map(wikiService::fetchArticle)
                .map(wikiService::parseMediaWikiText)
                //.observeOn(Schedulers.io())
        // 3. load and parse the article

                .flatMap(parsedPage -> {
                    Observable<Integer> ratedObservable = ratingService.rateObservable(parsedPage).subscribeOn(Schedulers.io());
                    Observable<Integer> countedObservable = countService.countWordsObservable(parsedPage).subscribeOn(Schedulers.io());

                    return Observable.zip(ratedObservable, countedObservable, (ro, co) ->
                            String.format(" \"rating\": \"%s\", \"wordCount\": \"%s\"",
                                    ro, co));
                })

                .subscribe(
                    next -> {
                        System.out.print(next);
                    }, System.err::println, monitor::complete);
        monitor.waitFor(10, TimeUnit.SECONDS);


        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS);
    }

}
