package com.senacor.codecamp.reactive.example.filtering;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class IgnoreElementsTest {
    @Test
    public void testIgnoreElements() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(1000, TimeUnit.MILLISECONDS)
                .ignoreElements()
                .subscribe(() -> print("complete!"),
                        Throwable::printStackTrace);
        Thread.sleep(1500);
    }
}
