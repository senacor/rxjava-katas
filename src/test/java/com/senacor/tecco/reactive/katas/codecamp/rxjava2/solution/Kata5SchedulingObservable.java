package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.PersistService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final PersistService persistService = PersistService.create();
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

        Scheduler fiveThreads = ReactiveUtil.newScheduler(5, "my-scheduler");

        Observable<ParsedPage> pages = wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .take(20)
                .flatMap(name -> Observable.just(name)
                        .flatMap(wikiService::fetchArticleObservable)
                        .subscribeOn(Schedulers.io()))
                .flatMap(wikiService::parseMediaWikiTextObservable);

        Observable<Integer> ratings = pages.flatMap(ratingService::rateObservable);
        Observable<Integer> wordCount = pages.flatMap(countService::countWordsObservable);

        Disposable subscription = ratings.zipWith(wordCount, (r, wc) -> String.format(
                "{\"rating\": %s, \"wordCount\": %s}",
                r, wc)
        )
                .subscribeOn(fiveThreads)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(22, TimeUnit.SECONDS);
        subscription.dispose();
    }

}
