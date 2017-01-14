package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    private final WikiService wikiService = new WikiService();

    @Test
    public void errors() throws Exception {
        // 1. use fetchArticleObservableWithRandomErrors which randomly has a Timeout (ERROR).
        // 2. handle error: use retries with increasing delays
        // 3. if retries fail, use a default.
        // 4. parse article with wikiService.parseMediaWikiText
        // 5. print parsedPage.getText to the console
        //
        // HINT: To test your retry/default behavior you can use Observable.error()

        TestSubscriber test = TestSubscriber.create();

/*        fetchArticleObservableWithRandomErrors("42")
                .doOnError(var -> System.out.println("Error"))
                .retry((count, error) -> Observable.just(count < 3)
                        .doOnNext(var -> {
                            if (var)
                                System.out.println("Retry!");
                            else
                                System.out.println("Aborting");
                        })
                        .delay((count % 3) * 500, TimeUnit.MILLISECONDS).toBlocking().single())
                .onErrorReturn(var -> "Fallback article")
                .map(wikiService::parseMediaWikiText)
                .map(ParsedPage::getText)
                .doOnNext(System.out::println)
                .subscribe(test);
*/
        final TempClass temp = new TempClass();

        fetchArticleObservableWithRandomErrors("42")
                .doOnError(var -> System.out.println("Error"))
                .retryWhen(attempts -> attempts.flatMap(error -> {
                    if (temp.counter < 3) {
                        temp.counter ++;
                        System.out.println("Retry!");
                        return Observable.timer(500*(temp.counter-1), TimeUnit.MILLISECONDS).map(x -> null);
                    }
                    System.out.println("Abort");
                    return Observable.error(error);
                }))
                .onErrorReturn(var -> "Fallback article")
                .map(wikiService::parseMediaWikiText)
                .map(ParsedPage::getText)
                .doOnNext(System.out::println)
                .subscribe(test);

        test.awaitTerminalEvent();
    }

    private class TempClass {
        public int counter = 0;
    }

    private Observable<String> fetchArticleObservableWithRandomErrors(String articleName) {
        final Random randomGenerator = new Random(12L);
        return wikiService.fetchArticleObservable(articleName).map(article -> {
            if (randomGenerator.nextInt() % 2 == 0) {
                throw new IllegalStateException("timeout");
            }
            return article;
        });
    }
}
