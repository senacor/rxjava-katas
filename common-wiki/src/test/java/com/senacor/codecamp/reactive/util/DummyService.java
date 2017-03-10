package com.senacor.codecamp.reactive.util;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Andreas Keefer
 */
public interface DummyService {
    int countWords(ParsedPage parsedPage);

    Mono<String> mono(String s);

    Flux<String> flux(String s);
}
