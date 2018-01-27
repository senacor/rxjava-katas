package com.senacor.codecamp.reactive.katas.codecamp.reactor.solution;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7bResilience {

    @Test
    public void backupOnError() throws Exception {
        // 1. use 'wikiService.fetchArticleFlux' to fetch an article. This Service always fails
        // 2. switch to the 'wikiServiceBackup.fetchArticleFlux' when the wikiService fails
        // 3. verify the behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(10),
                FlakinessFunction.alwaysFail());
        WikiService wikiServiceBackup = WikiService.create(DelayFunction.staticDelay(100));

        String wikiArticle = "42";
        StepVerifier.create(wikiService.fetchArticleFlux(wikiArticle)
                .onErrorResume(error -> wikiServiceBackup.fetchArticleFlux(wikiArticle))
                .subscribeOn(Schedulers.elastic()))
                .expectNextMatches(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"))
                .verifyComplete();
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
        StepVerifier.create(wikiService.fetchArticleFlux(wikiArticle)
                .onErrorResume(error -> wikiServiceBackup.fetchArticleFlux(wikiArticle))
                .onErrorReturn(getCachedArticle(wikiArticle))
                .subscribeOn(Schedulers.elastic()))
                .expectNext("{{Dieser Artikel|behandelt 42}} ")
                .verifyComplete();
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
        StepVerifier.create(wikiService.fetchArticleFlux(wikiArticle)
                .retryWhen(retryWithDelay(3, RetryDelay.exponential()))
                .onErrorResume(error -> wikiServiceBackup.fetchArticleFlux(wikiArticle))
                .onErrorReturn(getCachedArticle(wikiArticle))
                .subscribeOn(Schedulers.elastic()))
                .expectNextMatches(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"))
                .verifyComplete();
    }

    private java.util.function.Function<Flux<Throwable>, ? extends Publisher<?>> retryWithDelay(final int maxRetries,
                                                                                                RetryDelay retryDelay) {
        return attempts -> attempts.zipWith(Flux.range(1, maxRetries + 1), Kata7aResilience.ErrorWithRetryCount::new)
                .flatMap(
                        countAndError -> {
                            if (countAndError.getRetryCount() > maxRetries) {
                                throw Exceptions.propagate(countAndError.getThrowable());
                            }
                            long delay = retryDelay.delay(countAndError.getRetryCount());
                            print("ERROR ... retry in %s ms", delay);
                            Duration d = Duration
                                    .of(delay, ChronoUnit.MILLIS);
                            return Flux.interval(d).take(1);
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
