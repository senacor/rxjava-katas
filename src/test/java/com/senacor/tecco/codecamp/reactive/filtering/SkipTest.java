package com.senacor.tecco.codecamp.reactive.filtering;

import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class SkipTest {
    @Test
    public void testSkipNumber() throws Exception {
        Observable.just(1, 2, 3, 4)
                .skip(2)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2000, TimeUnit.MILLISECONDS)
                .skip(1000, TimeUnit.MILLISECONDS)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipWhile() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .skipWhile(value -> value < 5)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipLast() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .skipLast(3)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testSkipLastTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2000, TimeUnit.MILLISECONDS)
                .skipLast(1000, TimeUnit.MILLISECONDS)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }
}
