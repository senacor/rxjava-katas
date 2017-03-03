package com.senacor.tecco.reactive.example.filtering;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class SampleTest {

    @Test
    public void testSample() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3000, TimeUnit.MILLISECONDS)
                .sample(1000, TimeUnit.MILLISECONDS)
                .blockingForEach(next -> print("next: %s", next));
    }
}
