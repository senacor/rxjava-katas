package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution.Kata7aResilience.ErrorWithRetryCount;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.UncheckedIOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7aResilience {

    @Test
    public void timeout() throws Exception {
        // 1. use fetchArticleFluxWithTimeout and add there a timeout of 500ms.
        // 2. verify this behavior with a test

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(600));

        StepVerifier.create(fetchArticleFluxWithTimeout(wikiService, "42"))
                .expectError(TimeoutException.class)
                .verify();

        StepVerifier.create(fetchArticleFluxWithTimeout(WikiService.create(DelayFunction.staticDelay(400)), "42"))
                .expectNextMatches(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"))
                .verifyComplete();
    }

    private Flux<String> fetchArticleFluxWithTimeout(WikiService wikiService, String articleName) {
        return wikiService.fetchArticleFlux(articleName)
                .timeout(Duration.of(500, ChronoUnit.MILLIS))
                .subscribeOn(Schedulers.elastic());
    }

    @Test
    public void retryOK() throws Exception {
        // 3. when fetchArticleFluxWithTimeout fails, retry twice with a delay of 1 second
        // 4. verify this behavior with a test

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(100));

        StepVerifier.create(fetchArticleFluxWithTimeout(wikiService, "42")
                .retryWhen(retryWithDelay(2)))
                .expectNextMatches(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"))
                .verifyComplete();
    }

    @Test
    public void retryAlwaysFailed() throws Exception {
        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1),
                FlakinessFunction.alwaysFail());
        StepVerifier.create(fetchArticleFluxWithTimeout(wikiService, "42")
                .retryWhen(retryWithDelay(2)))
                .expectError(UncheckedIOException.class)
                .verify();
    }

    @Test
    public void retryCountdownOK() throws Exception {
        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1),
                FlakinessFunction.failCountDown(2));
        StepVerifier.create(fetchArticleFluxWithTimeout(wikiService, "42")
                .retryWhen(retryWithDelay(2)))
                .expectNextMatches(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"))
                .verifyComplete();
    }

    private java.util.function.Function<Flux<Throwable>, ? extends Publisher<?>> retryWithDelay(final int maxRetries) {
        return attempts -> attempts.zipWith(Flux.range(1, maxRetries + 1), ErrorWithRetryCount::new)
                .flatMap(
                        countAndError -> {
                            if (countAndError.getRetryCount() > maxRetries) {
                                throw Exceptions.propagate(countAndError.getThrowable());
                            }
                            print("randomDelay retry by %s second(s)", countAndError
                                    .getRetryCount());
                            Duration d = Duration
                                    .of(countAndError.getRetryCount(), ChronoUnit.SECONDS);
                            return Flux.interval(d).take(1);
                        });
    }

    @Test
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.
        Flux<String> timeout = fetchArticleFluxWithTimeout(WikiService.create(
                DelayFunction.staticDelay(600)), "42")
                .subscribeOn(Schedulers.elastic())
                .retryWhen(retryWithDelay(3));
        Flux<String> error = fetchArticleFluxWithTimeout(WikiService.create(
                DelayFunction.staticDelay(10), FlakinessFunction.alwaysFail()), "42")
                .subscribeOn(Schedulers.elastic())
                .retryWhen(retryWithDelay(3));
        Flux<String> ok = fetchArticleFluxWithTimeout(WikiService.create(
                DelayFunction.staticDelay(400)), "42")
                .subscribeOn(Schedulers.elastic())
                .retryWhen(retryWithDelay(3));

        StepVerifier.create(Flux.firstEmitting(Arrays.asList(timeout, error, ok))
                .subscribeOn(Schedulers.elastic()))
                .expectNextMatches(value -> value.startsWith("{{Dieser Artikel|behandelt das Jahr 42"))
                .verifyComplete();
    }
}
