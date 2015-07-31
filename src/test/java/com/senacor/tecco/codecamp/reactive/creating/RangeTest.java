package com.senacor.tecco.codecamp.reactive.creating;

import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class RangeTest {
    @Test
    public void testRange() throws Exception {
        Observable.range(4, 6)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testRangeWithScheduler() throws Exception {
        Observable.range(4, 6, Schedulers.computation())
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
        Thread.sleep(200);
    }
}
