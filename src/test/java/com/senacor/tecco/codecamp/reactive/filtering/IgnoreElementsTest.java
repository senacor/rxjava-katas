package com.senacor.tecco.codecamp.reactive.filtering;

import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class IgnoreElementsTest {
    @Test
    public void testIgnoreElements() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(1000, TimeUnit.MILLISECONDS)
                .ignoreElements()
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
        Thread.sleep(1500);
    }
}
