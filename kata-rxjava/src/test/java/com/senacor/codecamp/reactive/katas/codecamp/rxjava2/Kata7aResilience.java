package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.Paragraph;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;

/**
 * @author Andreas Keefer
 */
public class Kata7aResilience {

    @Test
    @KataClassification(mandatory)
    public void timeout() throws Exception {

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1000));

        // 1. use fetchArticleObservableWithTimeout and add there a timeout of 500ms.
        fetchArticleObservableWithTimeout(wikiService, "42")
                // 2. verify this behavior with tests
                .test()
                .assertError(TimeoutException.class);
    }

    private Observable<String> fetchArticleObservableWithTimeout(WikiService wikiService, String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .timeout(500, TimeUnit.MILLISECONDS);
    }

    @Test
    @KataClassification(hardcore)
    public void retry() throws Exception {
        // 3. when fetchArticleObservableWithTimeout fails, retry twice with a delay of 1 second
        // 4. verify this behavior with tests

        WaitMonitor waitMonitor = new WaitMonitor();

        Map<Integer, Throwable> failureMap = new HashMap<>();

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(3));

        fetchArticleObservableWithTimeout(wikiService, "42")
                .retryWhen(attempts ->
                        attempts.zipWith(Observable.range(1, 2),
                                (e, n) ->
                                {
                                    failureMap.put(n, e);
                                    return n;
                                })
                                .flatMap(
                                (n) -> {
                                    System.err.println(String.format("Attempt %d failed!", n));
                                    return Observable.timer(1, TimeUnit.SECONDS);
                                }
                        ))
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> {waitMonitor.complete();
                            System.out.println("Completed");}
                );


        waitMonitor.waitFor(5, TimeUnit.SECONDS);
        assert (failureMap.isEmpty());
    }

    @Test
    @KataClassification(nightmare)
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.
        WikiService wikiService = WikiService.create(DelayFunction.withRandomDelay(200, 1000));

        Observable<String> article1 = fetchArticleObservableWithTimeoutThreeTries(wikiService);
        Observable<String> article2 = fetchArticleObservableWithTimeoutThreeTries(wikiService);
        Observable<String> article3 = fetchArticleObservableWithTimeoutThreeTries(wikiService);

        article1.ambWith(article2)
                .ambWith(article3)
                .map(wikiService::parseMediaWikiText)
                .map(ParsedPage::getFirstParagraph)
                .map(Paragraph::getText)
                .subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Complete"));
    }

    private Observable<String> fetchArticleObservableWithTimeoutThreeTries(WikiService wikiService) {
        return fetchArticleObservableWithTimeout(wikiService, "42")
                .retryWhen(attempts ->
                        attempts.zipWith(Observable.range(1, 2),
                                (e, n) -> n)
                                .flatMap(
                                        (n) -> {
                                            System.err.println(String.format("Attempt %d failed!", n));
                                            return Observable.timer(n, TimeUnit.SECONDS);
                                        }
                                ))
                .subscribeOn(Schedulers.io());
    }
}
