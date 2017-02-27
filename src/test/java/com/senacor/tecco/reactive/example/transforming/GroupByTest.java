package com.senacor.tecco.reactive.example.transforming;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class GroupByTest {
    @Test
    public void testGroupBy() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .groupBy(index -> index % 2 == 0 ? "gerade" : "ungerade")
                .doOnNext(next -> {
                    print("next: %s", next);
                    next.subscribe(next2 -> print("key=" + next.getKey() + " value=" + next2));
                })
                .doOnComplete(() -> print("complete!"))
                .doOnError(Throwable::printStackTrace)
                .test()
                .awaitTerminalEvent(2, TimeUnit.SECONDS);
    }
}
