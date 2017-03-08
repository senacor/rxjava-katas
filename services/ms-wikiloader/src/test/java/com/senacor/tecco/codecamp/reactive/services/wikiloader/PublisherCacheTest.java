package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author Daniel Heinrich
 */
public class PublisherCacheTest {

    public static final int CACHE_SIZE = 20;

    AtomicInteger counter;
    PublisherCache<Integer, Integer> cache;

    @Before
    public void setup() {
        counter = new AtomicInteger();
        cache = new PublisherCache<>(producer(counter), CACHE_SIZE);
    }

    @Test
    public void shouldNotExecutePublisherOnInitialisation() {
        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void shouldNotExecutePublisherWhenNotSubscribing() {
        cache.lookup(0);
        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void shouldExecutePublisherOnTheFirstLookup() {
        getAndBlock(0);
        Assert.assertEquals(1, counter.get());
    }

    @Test
    public void shouldNotExecuteItAfterTheFirstLookup() {
        getAndBlock(0);
        getAndBlock(0);
        Assert.assertEquals(1, counter.get());
    }

    @Test
    public void shouldCExecutePublisherOnceForEachDistinctInput() {
        getAndBlock(0);
        getAndBlock(1);
        getAndBlock(2);
        getAndBlock(2);
        getAndBlock(2);
        Assert.assertEquals(3, counter.get());
    }

    @Test
    public void shouldReturnTheCachedValue() {
        for (int input = 0; input < CACHE_SIZE; input++) {
            Integer value = getAndBlock(input);
            for (int i = 0; i < 3; i++) {
                Assert.assertEquals(value, getAndBlock(input));
            }
        }
    }

    @Test
    public void shouldEvictLastUsedValueFromTheCache() {
        for (int input = 0; input < CACHE_SIZE + 1; input++) {
            getAndBlock(input);
        }
        Assert.assertEquals(CACHE_SIZE + 1, counter.get());

        //0 should have been evicted and this also should evict 1
        getAndBlock(0);
        Assert.assertEquals(CACHE_SIZE + 2, counter.get());

        //2 -> (CACHE_SIZE + 1) should still be in the cache
        for (int input = 2; input < CACHE_SIZE + 1; input++) {
            getAndBlock(input);
            Assert.assertEquals(CACHE_SIZE + 2, counter.get());
        }

        //should evict 2
        getAndBlock(CACHE_SIZE + 2);
        Assert.assertEquals(CACHE_SIZE + 3, counter.get());

        getAndBlock(1);
        getAndBlock(2);
        Assert.assertEquals(CACHE_SIZE + 5, counter.get());
    }

    private Integer getAndBlock(int id) {
        return cache.lookup(id).block();
    }

    private Function<Integer, Mono<Integer>> producer(AtomicInteger counter) {
        int[] value = {0};
        return i -> Mono.fromCallable(() -> {
            counter.incrementAndGet();
            return ++value[0];
        });
    }
}
