package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Andreas Keefer
 */
public interface CountService {

    static CountService create() {
        return create(DelayFunction.staticDelay(30),
                FlakinessFunction.noFlakiness());
    }

    static CountService create(DelayFunction delayFunction) {
        return create(delayFunction, FlakinessFunction.noFlakiness());
    }

    static CountService create(FlakinessFunction flakinessFunction) {
        return create(DelayFunction.staticDelay(30), flakinessFunction);
    }

    static CountService create(DelayFunction delayFunction,
                               FlakinessFunction flakinessFunction) {
        return new CountServiceImpl(flakinessFunction, delayFunction);
    }

    Observable<Integer> countWordsObservable(ParsedPage parsedPage);

    Flux<Integer> countWordsFlux(ParsedPage parsedPage);

    Future<Integer> countWordsFuture(ParsedPage parsedPage);

    CompletableFuture<Integer> countWordsCompletableFuture(ParsedPage parsedPage);

    int countWords(ParsedPage parsedPage);
}
