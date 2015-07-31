package com.senacor.tecco.codecamp.reactive.filtering;

import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class TakeTest {
    @Test
    public void testTakeNumber() throws Exception {
        Observable.just(1, 2, 3, 4)
                .take(2)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(1000, TimeUnit.MILLISECONDS)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeWhile() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 4, 3, 2, 1)
                .takeWhile(value -> value < 5)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeLast() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .takeLast(3)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeLastTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2000, TimeUnit.MILLISECONDS)
                .takeLast(1000, TimeUnit.MILLISECONDS)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }
}
