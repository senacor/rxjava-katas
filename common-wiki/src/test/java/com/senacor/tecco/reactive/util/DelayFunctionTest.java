package com.senacor.tecco.reactive.util;

import hu.akarnokd.rxjava2.math.ObservableAverageDouble;
import io.reactivex.Observable;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class DelayFunctionTest {
    @Test
    public void staticDelay() throws Exception {
        assertThat(DelayFunction.staticDelay(10).delay(""), is(10L));
        assertThat(DelayFunction.staticDelay(10, "test").delay("test"), is(10L));
        assertThat(DelayFunction.staticDelay(10, "test").delay("other"), is(0L));
    }

    @Test
    public void withNoDelay() throws Exception {
        assertThat(DelayFunction.withNoDelay().delay(""), is(0L));
    }

    @Test
    public void withRandomDelay() throws Exception {
        assertThat(withRandomDelay(10, 20, ""),
                allOf(greaterThanOrEqualTo(10L), lessThanOrEqualTo(20L)));
        assertThat(withRandomDelay(10, 20, "test", "test"),
                allOf(greaterThanOrEqualTo(10L), lessThanOrEqualTo(20L)));
        assertThat(withRandomDelay(10, 20, "other", "test"),
                is(0L));
    }

    @Test
    public void randomDelayAverage() throws Exception {
        new ObservableAverageDouble(Observable.range(1, 100)
                .map(i -> withRandomDelay(200, 1000, "")))
                .map(doubleVal -> BigDecimal.valueOf(doubleVal).setScale(0, RoundingMode.HALF_UP).intValue())
                .doOnNext(next -> print("res=" + next))
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(value -> value >= 500 && value <= 700);
    }

    long withRandomDelay(int lowerBoundMillis, int upperBoundMillis, String method, String... methodNames) {
        long delay = DelayFunction.withRandomDelay(lowerBoundMillis, upperBoundMillis, methodNames).delay(method);
        System.out.println("RandomDelay=" + delay);
        return delay;
    }
}