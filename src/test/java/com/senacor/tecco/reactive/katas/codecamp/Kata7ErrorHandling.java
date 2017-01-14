package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

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
        final WaitMonitor monitor = new WaitMonitor();

        fetchArticleObservableWithRandomErrors("42")
            .retryWhen(new RetryWithDelay(3))
            .onErrorResumeNext(wikiService.fetchArticleObservable("Flugzeug"))
            .flatMap(wikiService::parseMediaWikiTextObservable)
            .map(ParsedPage::getText)
            .subscribe(
                    System.out::println,
                    Throwable::printStackTrace
            );

        monitor.waitFor(20, TimeUnit.SECONDS);
    }


    public class RetryWithDelay implements Func1<Observable<? extends Throwable>, Observable<?>> {

        private final int maxRetries;
        private int retryCount;

        public RetryWithDelay(final int maxRetries) {
            this.maxRetries = maxRetries;
            this.retryCount = 0;
        }

        @Override
        public Observable<?> call(Observable<? extends Throwable> attempts) {
            return attempts
                .flatMap(throwable -> {
                    if (++retryCount <= maxRetries) {
                        System.out.println("Retry with delay " + retryCount + " seconds");
                        return Observable.timer(retryCount, TimeUnit.SECONDS);
                    }
                    return Observable.error(throwable);
                });
        }
    }


    private Observable<String> fetchArticleObservableWithRandomErrors(String articleName) {
        final Random randomGenerator = new Random(12L);
        return wikiService.fetchArticleObservable(articleName).map(article -> {


            /*if (randomGenerator.nextInt() % 2 == 0) {
                throw new IllegalStateException("timeout");
            }*/
            System.out.println("Error!");
            throw new IllegalStateException("timeout");
        });
    }

}
