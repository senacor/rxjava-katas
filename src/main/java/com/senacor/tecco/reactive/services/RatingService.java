package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Andreas Keefer
 */
public interface RatingService {

    static RatingService create() {
        return create(DelayFunction.staticDelay(30),
                FlakinessFunction.noFlakiness());
    }

    static RatingService create(DelayFunction delayFunction) {
        return create(delayFunction, FlakinessFunction.noFlakiness());
    }

    static RatingService create(FlakinessFunction flakinessFunction) {
        return create(DelayFunction.staticDelay(30), flakinessFunction);
    }

    static RatingService create(DelayFunction delayFunction,
                                       FlakinessFunction flakinessFunction) {
        return StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new RatingServiceImpl(), flakinessFunction)
                        , delayFunction));
    }

    Observable<Integer> rateObservable(ParsedPage parsedPage);

    Flux<Integer> rateFlux(ParsedPage parsedPage);

    Future<Integer> rateFuture(ParsedPage parsedPage);

    CompletableFuture<Integer> rateCompletableFuture(ParsedPage parsedPage);

    int rate(ParsedPage parsedPage);
}
