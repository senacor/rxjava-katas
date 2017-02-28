package com.senacor.tecco.reactive.example.filtering;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class TakeTest {
    @Test
    public void testTakeNumber() throws Exception {
        Observable.just(1, 2, 3, 4)
                .take(2)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(1000, TimeUnit.MILLISECONDS)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeWhile() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 4, 3, 2, 1)
                .takeWhile(value -> value < 5)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeLast() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .takeLast(3)
                .blockingForEach(next -> print("next: %s", next));
    }

    @Test
    public void testTakeLastTimeInterval() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2000, TimeUnit.MILLISECONDS)
                .takeLast(1000, TimeUnit.MILLISECONDS)
                .blockingForEach(next -> print("next: %s", next));
    }
}
