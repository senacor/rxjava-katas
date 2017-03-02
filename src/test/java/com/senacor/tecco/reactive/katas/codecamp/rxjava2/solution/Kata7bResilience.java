package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution.Kata7aResilience.ErrorWithRetryCount;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7bResilience {

    @Test
    public void backupOnError() throws Exception {
        // 1. use 'wikiService.fetchArticleObservable' to fetch an article. This Service always fails
        // 2. switch to the 'wikiServiceBackup.fetchArticleObservable' when the wikiService fails
        // 3. verify the behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(10),
                FlakinessFunction.alwaysFail());
        WikiService wikiServiceBackup = WikiService.create(DelayFunction.staticDelay(100));

        String wikiArticle = "42";
        wikiService.fetchArticleObservable(wikiArticle)
                .onErrorResumeNext(wikiServiceBackup.fetchArticleObservable(wikiArticle))
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
    }

    @Test
    public void defaultValueBackup() throws Exception {
        // 4. if the call to the 'wikiServiceBackup' also fails, return a default value (e.g. 'getCachedArticle')
        // 5. verify the behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(10),
                FlakinessFunction.alwaysFail());
        WikiService wikiServiceBackup = WikiService.create(DelayFunction.staticDelay(100),
                FlakinessFunction.alwaysFail());

        final String wikiArticle = "42";
        wikiService.fetchArticleObservable(wikiArticle)
                .onErrorResumeNext(wikiServiceBackup.fetchArticleObservable(wikiArticle))
                .onErrorReturnItem(getCachedArticle(wikiArticle))
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue("{{Dieser Artikel|behandelt 42}} ");
    }

    private String getCachedArticle(String articleName) {
        print("getCachedArticle: '%s'", articleName);
        return "{{Dieser Artikel|behandelt " + articleName + "}} ";
    }

    @Test
    public void exponentialRetry() throws Exception {
        // 6. insert in this example a retry strategy: 3 retries with an exponential back-off
        //    (e.g wait 100ms for the first retry, 400ms for the second retry and 900ms for the 3rd retry)

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(10),
                FlakinessFunction.failCountDown(3));
        WikiService wikiServiceBackup = WikiService.create(DelayFunction.staticDelay(100),
                FlakinessFunction.alwaysFail());

        final String wikiArticle = "42";
        wikiService.fetchArticleObservable(wikiArticle)
                .retryWhen(retryWithDelay(3, RetryDelay.exponential()))
                .onErrorResumeNext(wikiServiceBackup.fetchArticleObservable(wikiArticle))
                .onErrorReturnItem(getCachedArticle(wikiArticle))
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
    }

    private Function<? super Observable<Throwable>, ? extends ObservableSource<?>> retryWithDelay(final int maxRetries,
                                                                                                  final RetryDelay retryDelay) {
        return attempts -> attempts.zipWith(Observable.range(1, maxRetries + 1), ErrorWithRetryCount::new)
                .flatMap(
                        countAndError -> {
                            if (countAndError.getRetryCount() > maxRetries) {
                                return Observable.error(countAndError.getThrowable());
                            }
                            long delay = retryDelay.delay(countAndError.getRetryCount());
                            print("ERROR ... retry in %s ms", delay);
                            return Observable
                                    .timer(delay, TimeUnit.MILLISECONDS);
                        });
    }

    @FunctionalInterface
    public interface RetryDelay {
        /**
         * @param retryCount the current retry count
         * @return delay in millis
         */
        long delay(int retryCount);

        static RetryDelay exponential() {
            return (int retryCount) -> retryCount * retryCount * 100;
        }
    }
}
