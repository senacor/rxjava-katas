package com.senacor.tecco.reactive.services;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Andreas Keefer
 */
public interface CountService {
    Observable<Integer> countWordsObservable(ParsedPage parsedPage);

    Flux<Integer> countWordsFlux(ParsedPage parsedPage);

    Future<Integer> countWordsFuture(ParsedPage parsedPage);

    CompletableFuture<Integer> countWordsCompletableFuture(ParsedPage parsedPage);

    int countWords(ParsedPage parsedPage);
}
