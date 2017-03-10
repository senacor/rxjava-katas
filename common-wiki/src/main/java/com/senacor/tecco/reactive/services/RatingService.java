package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.services.integration.RatingBackend;
import com.senacor.tecco.reactive.services.integration.RatingBackendImpl;
import com.senacor.tecco.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RatingService {

    private final RatingBackend ratingBackend;

    public static RatingService create() {
        return create(DelayFunction.staticDelay(30),
                FlakinessFunction.noFlakiness());
    }

    public static RatingService create(DelayFunction delayFunction) {
        return create(delayFunction, FlakinessFunction.noFlakiness());
    }

    public static RatingService create(FlakinessFunction flakinessFunction) {
        return create(DelayFunction.staticDelay(30), flakinessFunction);
    }

    public static RatingService create(DelayFunction delayFunction,
                                       FlakinessFunction flakinessFunction) {
        return new RatingService(flakinessFunction, delayFunction);
    }


    private RatingService(FlakinessFunction flakinessFunction, DelayFunction delayFunction) {
        ratingBackend = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new RatingBackendImpl(), flakinessFunction)
                        , delayFunction));
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public Observable<Integer> rateObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::rate);
    }

    public Flux<Integer> rateFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::rate);
    }

    public Future<Integer> rateFuture(ParsedPage parsedPage) {
        return pool.submit(() -> rate(parsedPage));
    }

    public CompletableFuture<Integer> rateCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> rate(parsedPage), pool);
    }

    /**
     * @return a rating of the wiki article from 1 to 5 'stars'
     */
    public int rate(final ParsedPage parsedPage) {
        return ratingBackend.rate(parsedPage);
    }
}
