package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Daniel Heinrich
 */
public class PublisherCache<I, O> {

    private static final int DEFAULT_CACHE_SIZE = 20;

    private final Cache<I, O> cache;
    private final Function<I, Mono<O>> transformer;

    public PublisherCache(Function<I, Mono<O>> transformer) {
        this(transformer, DEFAULT_CACHE_SIZE);
    }

    public PublisherCache(Function<I, Mono<O>> transformer, int cacheSize) {
        this.transformer = transformer;
        cache = CacheBuilder.newBuilder().maximumSize(cacheSize).build();
    }

    public Mono<O> lookup(I input) {
        return Mono.justOrEmpty(cache.getIfPresent(input))
                .doOnNext(i -> print("cache hit for key '%s'", input))
                .otherwiseIfEmpty(transformer.apply(input)
                        .doOnNext(o -> cache.put(input, o))
                );
    }
}
