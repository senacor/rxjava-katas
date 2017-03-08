package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import org.apache.commons.collections4.map.LRUMap;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Daniel Heinrich
 */
public class PublisherCache<I, O> {

    private static final int DEFAULT_CACHE_SIZE = 20;

    private final Map<I, O> cache;
    private final Function<I, Mono<O>> transformer;

    public PublisherCache(Function<I, Mono<O>> transformer) {
        this(transformer, DEFAULT_CACHE_SIZE);
    }

    public PublisherCache(Function<I, Mono<O>> transformer, int cacheSize) {
        this.transformer = transformer;
        cache = new LRUMap<>(cacheSize);
    }

    public Mono<O> lookup(I input) {
        return Mono.justOrEmpty(cache.get(input))
                .doOnNext(i -> print("cache hit for key '%s'", input))
                .otherwiseIfEmpty(transformer.apply(input)
                        .doOnNext(o -> cache.put(input, o))
                );
    }
}
