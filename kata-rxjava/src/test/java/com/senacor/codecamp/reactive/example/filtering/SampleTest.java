package com.senacor.codecamp.reactive.example.filtering;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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
                .blockingForEach(next -> ReactiveUtil.print("next: %s", next));
    }
}
