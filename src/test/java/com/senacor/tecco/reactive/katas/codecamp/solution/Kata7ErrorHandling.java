package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    private final WikiService wikiService = new WikiService();

    @Test
    public void errors() throws Exception {
        // 1. use fetchArticleObservableWithRandomErrors that that randomly has a Timeout (ERROR).
        // 2. handle error: use retries with increasing delays
        // 3. if retries fail, use a default.
        // 4. parse article with wikiService.parseMediaWikiText
        // 5. print parsedPage.getText to the console
        //
        // HINT: To test your retry/default behavior you can use Observable.error()


        final WaitMonitor monitor = new WaitMonitor();
        final String articleName = "42";
        Subscription subscription = fetchArticleObservableWithRandomErrors(articleName)
                // delayed retry
                .retryWhen(retryWithDelay(3))
                // default on error
                .onErrorReturn(throwable -> getCachedArticle(articleName))
                .map(wikiService::parseMediaWikiText)
                .subscribe(next -> print("next: %s", next.getText()),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(20, TimeUnit.SECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void errorsWithDefaultTest() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        final String articleName = "42";
        Subscription subscription = Observable.<String>error(new IllegalStateException("timeout"))
                // delayed retry
                .retryWhen(retryWithDelay(3))
                // default on error
                .onErrorReturn(throwable -> getCachedArticle(articleName))
                .map(wikiService::parseMediaWikiText)
                .subscribe(next -> print("next: %s", next.getText()),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(20, TimeUnit.SECONDS);
        subscription.unsubscribe();
    }

    private String getCachedArticle(String articleName) {
        print("getCachedArticle: '%s'", articleName);
        return "{{Dieser Artikel|behandelt das Jahr 42}} ";
    }

    private Func1<Observable<? extends Throwable>, Observable<?>> retryWithDelay(final int maxRetries) {
        return attempts -> attempts.zipWith(Observable.range(1, maxRetries + 1), ErrorWithRetryCount::new)
                .flatMap(
                        countAndError -> {
                            if (countAndError.getRetryCount() > maxRetries) {
                                return Observable.error(countAndError.getThrowable());
                            }
                            print("randomDelay retry by %s second(s)", countAndError.getRetryCount());
                            return Observable.timer((long) countAndError.getRetryCount(), TimeUnit.SECONDS);
                        });
    }

    private static class ErrorWithRetryCount {
        private final Throwable throwable;
        private final int retryCount;

        private ErrorWithRetryCount(Throwable throwable, int retryCount) {
            this.throwable = throwable;
            this.retryCount = retryCount;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public int getRetryCount() {
            return retryCount;
        }
    }

    private Observable<String> fetchArticleObservableWithRandomErrors(String articleName) {
        final Random randomGenerator = new Random();
        return wikiService.fetchArticleObservable(articleName).map(article -> {
            if (randomGenerator.nextInt() % 2 == 0) {
                throw new IllegalStateException("timeout");
            }
            return article;
        });
    }
}
