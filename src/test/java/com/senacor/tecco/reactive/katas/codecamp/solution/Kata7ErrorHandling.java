package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    private final WikiService wikiService = new WikiService();

    @Test
    public void errors() throws Exception {
        // 1. use WikiService#wikiArticleBeingReadObservableWithRandomErrors that creates a stream of wiki article names being read.
        // 2. filter burst.
        // 3. handle error: use retries with increasing delays
        // 4. if retries fail, terminate stream with a default

        final WaitMonitor monitor = new WaitMonitor();
        Subscription subscription = wikiService.wikiArticleBeingReadObservableWithRandomErrors()
                .take(20)
                .debounce(50, TimeUnit.MILLISECONDS)
                // delayed retry
                .retryWhen(retryWithDelay(3))
                // default on error
                .onErrorReturn(throwable -> "default on error")
                .subscribe(next -> print("next: %s", next),
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
        Subscription subscription = Observable.error(new IllegalStateException("something's wrong"))
                // delayed retry
                .retryWhen(retryWithDelay(3))
                // default on error
                .onErrorReturn(throwable -> "default on error")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(20, TimeUnit.SECONDS);
        subscription.unsubscribe();
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
}
