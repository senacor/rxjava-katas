package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static reactor.core.scheduler.Schedulers.elastic;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final PersistService persistService = PersistService.create();
    private final CountService countService = CountService.create();

    /**
     * 1. use the {@link WikiServiceImpl#wikiArticleBeingReadFlux(long, TimeUnit)} to create a stream of
     * wiki article names being read
     * 2. take only the first 20 articles
     * 3. load and parse the article
     * 4. use the {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}
     * to combine both as JSON and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
     * 5. measure the runtime
     * 6. add a scheduler to a specific position in the observable chain to reduce the execution time
     */
    @Test
    public void schedulingObservable() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Flux<String> articles = wikiService.wikiArticleBeingReadFlux(50, TimeUnit.MILLISECONDS);
        Flux<ParsedPage> pages = articles
                .take(20)
                .flatMap(name -> wikiService.fetchArticleFlux(name)).subscribeOn(elastic())
                .flatMap(wikiService::parseMediaWikiTextFlux);

        Scheduler fiveThreads = Schedulers.newParallel("my-scheduler", 5);
        Flux<Integer> ratings = pages.flatMap(ratingService::rateFlux).subscribeOn(fiveThreads);
        Flux<Integer> wordCount = pages.flatMap(countService::countWordsFlux).subscribeOn(fiveThreads);

        Disposable subscribtion = ratings.zipWith(wordCount)
                                         .subscribe(next -> print("next: %s", next),
                                                 Throwable::printStackTrace,
                                                 () -> {
                                                     print("complete!");
                                                     monitor.complete();
                                                 });

        monitor.waitFor(22, TimeUnit.SECONDS);
        subscribtion.dispose();
    }
}