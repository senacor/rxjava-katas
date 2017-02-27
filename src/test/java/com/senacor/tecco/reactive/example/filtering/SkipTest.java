package com.senacor.tecco.reactive.example.filtering;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class SkipTest {
    @Test
    public void testSkipNumber() throws Exception {
        Observable.just(1, 2, 3, 4)
                .skip(2)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2000, TimeUnit.MILLISECONDS)
                .skip(1000, TimeUnit.MILLISECONDS)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipWhile() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .skipWhile(value -> value < 5)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipLast() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .skipLast(3)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipLastTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2000, TimeUnit.MILLISECONDS)
                .skipLast(1000, TimeUnit.MILLISECONDS)
                .blockingForEach(next -> print("next: %s", next));
    }
}
