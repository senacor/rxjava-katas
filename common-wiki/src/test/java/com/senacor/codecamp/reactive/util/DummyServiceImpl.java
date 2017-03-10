package com.senacor.codecamp.reactive.util;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.StringTokenizer;

/**
 * @author Andreas Keefer
 */
public class DummyServiceImpl implements DummyService {
    @Override
    public int countWords(final ParsedPage parsedPage) {
        return new StringTokenizer(parsedPage.getText(), " ").countTokens();
    }

    @Override
    public Mono<String> mono(String s) {
        return Mono.just(s);
    }

    @Override
    public Flux<String> flux(String s) {
        return Flux.range(0, 10)
                .map(count -> s + count);
    }
}
