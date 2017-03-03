package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7aResilience {

    @Test
    public void timeout() throws Exception {
        // 1. use fetchArticleObservableWithTimeout and add there a timeout of 500ms.
        // 2. verify this behavior with a test

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(600));

        fetchArticleObservableWithTimeout(wikiService, "42")
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertError(TimeoutException.class);

        fetchArticleObservableWithTimeout(WikiService.create(DelayFunction.staticDelay(400)), "42")
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertComplete()
                .assertValue(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
    }

    private Observable<String> fetchArticleObservableWithTimeout(WikiService wikiService, String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .timeout(500, TimeUnit.MILLISECONDS);
    }

    @Test
    public void retryOK() throws Exception {
        // 3. when fetchArticleObservableWithTimeout fails, retry twice with a delay of 1 second
        // 4. verify this behavior with a test

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(100));

        fetchArticleObservableWithTimeout(wikiService, "42")
                .retryWhen(retryWithDelay(2))
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertComplete()
                .assertValue(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
    }

    @Test
    public void retryAlwaysFailed() throws Exception {
        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1),
                FlakinessFunction.alwaysFail());
        fetchArticleObservableWithTimeout(wikiService, "42")
                .retryWhen(retryWithDelay(2))
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertError(UncheckedIOException.class)
                .assertTerminated();
    }

    @Test
    public void retryCountdownOK() throws Exception {
        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1),
                FlakinessFunction.failCountDown(2));
        fetchArticleObservableWithTimeout(wikiService, "42")
                .retryWhen(retryWithDelay(2))
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertComplete();
    }

    private Function<? super Observable<Throwable>, ? extends ObservableSource<?>> retryWithDelay(final int maxRetries) {
        return attempts -> attempts.zipWith(Observable.range(1, maxRetries + 1), ErrorWithRetryCount::new)
                .flatMap(
                        countAndError -> {
                            if (countAndError.getRetryCount() > maxRetries) {
                                return Observable.error(countAndError.getThrowable());
                            }

                            int retryDelay = countAndError.getRetryCount();
                            print("ERROR ... retry in %s second(s)", retryDelay);
                            return Observable
                                    .timer(retryDelay, TimeUnit.SECONDS);
                        });
    }

    public static class ErrorWithRetryCount {
        private final Throwable throwable;
        private final int retryCount;

        public ErrorWithRetryCount(Throwable throwable, int retryCount) {
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

    @Test
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.
        Observable<String> timeout = fetchArticleObservableWithTimeout(WikiService.create(
                DelayFunction.staticDelay(600)), "42")
                .subscribeOn(Schedulers.io())
                .retryWhen(retryWithDelay(3));
        Observable<String> error = fetchArticleObservableWithTimeout(WikiService.create(
                DelayFunction.staticDelay(10), FlakinessFunction.alwaysFail()), "42")
                .subscribeOn(Schedulers.io())
                .retryWhen(retryWithDelay(3));
        Observable<String> ok = fetchArticleObservableWithTimeout(WikiService.create(
                DelayFunction.staticDelay(400)), "42")
                .subscribeOn(Schedulers.io())
                .retryWhen(retryWithDelay(3));

        Observable.amb(Arrays.asList(timeout, error, ok))
                .subscribeOn(Schedulers.io())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
    }
}
