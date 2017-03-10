package com.senacor.codecamp.reactive.katas.codecamp.reactor.solution;

import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata5Scheduling {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    /**
     * 1. use the {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} to create a stream of
     * wiki article names being read
     * 2. take only the first 20 articles
     * 3. load and parse the article
     * 4. use the {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}
     * to combine both as JSON and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
     * 5. measure the runtime
     * 6. add a scheduler to a specific position in the chain to reduce the execution time
     */
    @Test
    public void scheduling() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Scheduler fiveThreads = ReactiveUtil.newReactorScheduler(5, "my-scheduler");

        Disposable subscription = wikiService.wikiArticleBeingReadFlux(50, TimeUnit.MILLISECONDS)
                .take(20)
                .flatMap(name -> wikiService.fetchArticleFlux(name).subscribeOn(Schedulers.elastic()))
                .flatMap(wikiService::parseMediaWikiTextFlux)
                .flatMap(parsedPage -> Flux.zip(ratingService.rateFlux(parsedPage).subscribeOn(fiveThreads),
                        countService.countWordsFlux(parsedPage).subscribeOn(fiveThreads),
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