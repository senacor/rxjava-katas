package com.senacor.codecamp.reactive.services;

import com.senacor.codecamp.reactive.services.integration.CounterBackend;
import com.senacor.codecamp.reactive.services.integration.CounterBackendImpl;
import com.senacor.codecamp.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CountService {

    private final CounterBackend counterBackend;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static CountService create() {
        return create(DelayFunction.staticDelay(30),
                FlakinessFunction.noFlakiness());
    }

    public static CountService create(DelayFunction delayFunction) {
        return create(delayFunction, FlakinessFunction.noFlakiness());
    }

    public static CountService create(FlakinessFunction flakinessFunction) {
        return create(DelayFunction.staticDelay(30), flakinessFunction);
    }

    public static CountService create(DelayFunction delayFunction,
                                      FlakinessFunction flakinessFunction) {
        return new CountService(flakinessFunction, delayFunction);
    }

    private CountService(FlakinessFunction flakinessFunction, DelayFunction delayFunction) {
        this.counterBackend = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new CounterBackendImpl(), flakinessFunction)
                        , delayFunction));
    }

    public Observable<Integer> countWordsObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::countWords);
    }

    public Flux<Integer> countWordsFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::countWords);
    }

    public Flowable<Integer> countWordsFlowable(final ParsedPage parsedPage) {
        return Flowable.just(parsedPage).map(this::countWords);
    }

    public Future<Integer> countWordsFuture(final ParsedPage parsedPage) {
        return pool.submit(() -> countWords(parsedPage));
    }


    public CompletableFuture<Integer> countWordsCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> countWords(parsedPage), pool);
    }

    /**
     * @return count of all words in this article
     */
    public int countWords(final ParsedPage parsedPage) {
        return counterBackend.countWords(parsedPage);
    }
}
