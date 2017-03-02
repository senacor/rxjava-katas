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

public class RatingServiceImpl implements RatingService {

    private final RatingBackend ratingBackend;

    RatingServiceImpl(FlakinessFunction flakinessFunction, DelayFunction delayFunction) {
        ratingBackend = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new RatingBackendImpl(), flakinessFunction)
                        , delayFunction));
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    @Override
    public Observable<Integer> rateObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::rate);
    }

    @Override
    public Flux<Integer> rateFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::rate);
    }

    @Override
    public Future<Integer> rateFuture(ParsedPage parsedPage) {
        return pool.submit(() -> rate(parsedPage));
    }

    @Override
    public CompletableFuture<Integer> rateCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> rate(parsedPage), pool);
    }

    @Override
    public int rate(final ParsedPage parsedPage) {
        return ratingBackend.rate(parsedPage);
    }
}
