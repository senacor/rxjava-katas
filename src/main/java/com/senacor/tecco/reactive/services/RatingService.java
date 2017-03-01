package com.senacor.tecco.reactive.services;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Andreas Keefer
 */
public interface RatingService {
    Observable<Integer> rateObservable(ParsedPage parsedPage);

    Flux<Integer> rateFlux(ParsedPage parsedPage);

    Future<Integer> rateFuture(ParsedPage parsedPage);

    CompletableFuture<Integer> rateCompletableFuture(ParsedPage parsedPage);

    int rate(ParsedPage parsedPage);
}
