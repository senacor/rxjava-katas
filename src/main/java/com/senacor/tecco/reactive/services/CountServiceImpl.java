package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.services.integration.CounterBackend;
import com.senacor.tecco.reactive.services.integration.CounterBackendImpl;
import com.senacor.tecco.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CountServiceImpl implements CountService {

    private final CounterBackend counterBackend;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    CountServiceImpl(FlakinessFunction flakinessFunction, DelayFunction delayFunction) {
        this.counterBackend = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new CounterBackendImpl(), flakinessFunction)
                        , delayFunction));
    }

    @Override
    public Observable<Integer> countWordsObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::countWords);
    }

    @Override
    public Flux<Integer> countWordsFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::countWords);
    }

    @Override
    public Future<Integer> countWordsFuture(final ParsedPage parsedPage) {
        return pool.submit(() -> countWords(parsedPage));
    }


    @Override
    public CompletableFuture<Integer> countWordsCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> countWords(parsedPage), pool);
    }

    @Override
    public int countWords(final ParsedPage parsedPage) {
        return counterBackend.countWords(parsedPage);
    }
}
